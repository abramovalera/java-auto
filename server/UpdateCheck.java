import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * «Вышло обновление курса?» — сравнивает коммит, на котором стоит папка курса, с последним коммитом на GitHub.
 *
 * Папка курса подключена в контейнер только для чтения (COURSE_DIR, см. docker-compose.yml): отсюда читаем .git/HEAD.
 * GitHub спрашиваем без токена (60 запросов в час с одного IP), поэтому ответ кэшируется на 30 минут —
 * сколько бы вкладок ни было открыто, к GitHub уходит не больше двух запросов в полчаса.
 */
final class UpdateCheck {
    private static final Duration OK_TTL = Duration.ofMinutes(30);
    private static final Duration ERROR_TTL = Duration.ofMinutes(10);
    private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();

    private static Map<String, Object> cached;
    private static long cachedUntil;

    private UpdateCheck() {}

    static synchronized Map<String, Object> status(Path courseDir, String repo) {
        long now = System.currentTimeMillis();
        if (cached != null && now < cachedUntil) return cached;
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("repo", repo);
        r.put("checkedAt", now);
        boolean ok;
        try {
            ok = fill(r, courseDir, repo);
        } catch (Exception e) {
            r.put("status", "error");
            r.put("message", "Не удалось спросить GitHub: " + e.getMessage());
            ok = false;
        }
        cached = r;
        cachedUntil = now + (ok ? OK_TTL : ERROR_TTL).toMillis();
        return r;
    }

    /** true — ответ надёжный, можно кэшировать надолго. */
    private static boolean fill(Map<String, Object> r, Path courseDir, String repo) throws Exception {
        Path git = courseDir.resolve(".git");
        if (!Files.isDirectory(git)) {
            r.put("mode", "zip"); // курс скачан архивом — своей версии не знаем
            r.put("status", "unknown");
            return true;
        }
        r.put("mode", "git");
        String[] head = localHead(git);
        String local = head[0], branch = head[1];
        if (local == null) {
            r.put("status", "unknown");
            return true;
        }
        r.put("local", local.substring(0, 7));
        r.put("branch", branch);

        HttpResponse<String> resp = get("https://api.github.com/repos/" + repo + "/compare/" + local + "..." + branch);
        if (resp.statusCode() == 404) { // коммита нет на GitHub (локальные правки автора) или ветки нет
            r.put("status", "unknown");
            return true;
        }
        if (resp.statusCode() != 200) {
            r.put("status", "error");
            r.put("message", "GitHub ответил " + resp.statusCode());
            return false;
        }
        Map<?, ?> cmp = (Map<?, ?>) Json.parse(resp.body());
        String st = String.valueOf(cmp.get("status")); // identical | behind | ahead | diverged (с точки зрения ветки на GitHub)
        int newCommits = num(cmp.get("ahead_by")); // сколько коммитов есть на GitHub и нет у нас
        r.put("status", newCommits > 0 ? "behind" : "current");
        r.put("newCommits", newCommits);
        r.put("githubStatus", st);
        List<Map<String, Object>> commits = new ArrayList<>();
        Object list = cmp.get("commits");
        if (list instanceof List<?> l) {
            for (int i = l.size() - 1; i >= 0 && commits.size() < 15; i--) { // свежие сверху
                Map<?, ?> c = (Map<?, ?>) l.get(i);
                Map<?, ?> commit = (Map<?, ?>) c.get("commit");
                Map<?, ?> author = commit == null ? null : (Map<?, ?>) commit.get("author");
                String msg = commit == null ? "" : String.valueOf(commit.get("message"));
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("sha", String.valueOf(c.get("sha")).substring(0, 7));
                m.put("title", msg.split("\n", 2)[0]);
                m.put("body", msg.contains("\n") ? msg.split("\n", 2)[1].strip() : "");
                m.put("date", author == null ? "" : String.valueOf(author.get("date")));
                commits.add(m);
            }
        }
        r.put("commits", commits);
        return true;
    }

    /** {sha, branch} из .git/HEAD — без программы git, её в контейнере нет. */
    private static String[] localHead(Path git) throws Exception {
        String head = Files.readString(git.resolve("HEAD"), StandardCharsets.UTF_8).strip();
        if (!head.startsWith("ref: ")) return new String[]{head.matches("[0-9a-f]{40}") ? head : null, "main"};
        String ref = head.substring(5).strip(); // refs/heads/main
        String branch = ref.startsWith("refs/heads/") ? ref.substring("refs/heads/".length()) : "main";
        Path refFile = git.resolve(ref);
        if (Files.isRegularFile(refFile)) return new String[]{Files.readString(refFile).strip(), branch};
        Path packed = git.resolve("packed-refs");
        if (Files.isRegularFile(packed)) {
            for (String line : Files.readAllLines(packed, StandardCharsets.UTF_8)) {
                if (line.endsWith(" " + ref)) return new String[]{line.substring(0, 40), branch};
            }
        }
        return new String[]{null, branch};
    }

    private static HttpResponse<String> get(String url) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "java-course-update-check")
                .GET().build();
        return HTTP.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private static int num(Object o) {
        if (o instanceof Number n) return n.intValue();
        try { return Integer.parseInt(String.valueOf(o)); } catch (NumberFormatException e) { return 0; }
    }
}
