import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Маленький сервер курса «Java с нуля».
 * <p>
 * Раздаёт сайт (папка site/) и проверяет решения задач:
 * берёт файл ученика из workspace/, компилирует его javac, запускает java,
 * подаёт входные данные и сравнивает вывод с ожидаемым.
 * <p>
 * Написан только на стандартной библиотеке Java — без фреймворков.
 * Когда пройдёшь модули про классы, коллекции и файлы, сможешь прочитать его целиком.
 */
public class CourseServer {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final int RUN_TIMEOUT_SEC = 5;
    private static final int COMPILE_TIMEOUT_SEC = 30;

    private static Path siteDir;
    private static Path tasksDir;
    private static Path workspaceDir;
    private static Path starterDir;
    private static Path projectDir;   // сквозной проект ученика «МиниБанк» (своя папка и свой Git)
    private static ProgressStore progress;

    public static void main(String[] args) throws IOException {
        siteDir = Path.of(env("SITE_DIR", "site")).toAbsolutePath().normalize();
        tasksDir = Path.of(env("TASKS_DIR", "tasks")).toAbsolutePath().normalize();
        workspaceDir = Path.of(env("WORKSPACE_DIR", "workspace")).toAbsolutePath().normalize();
        starterDir = Path.of(env("STARTER_DIR", "starter")).toAbsolutePath().normalize();
        projectDir = Path.of(env("PROJECT_DIR", "MiniBank")).toAbsolutePath().normalize();
        int port = Integer.parseInt(env("PORT", "7070"));
        progress = new ProgressStore(workspaceDir.resolve(".progress")); // старый общий прогресс (до профилей)
        copyStarters("module-01"); // заготовки остальных модулей — когда ученик откроет их задачи
        ensureModuleFolders();
        ensureIdeaProject();
        ensureTrainerFolders();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/tasks", ex -> safe(ex, CourseServer::handleTasks));
        server.createContext("/api/status", ex -> safe(ex, CourseServer::handleStatus));
        // проверки (javac + запуск) — не больше 4 одновременно, чтобы страницы и статика не ждали в очереди за ними
        server.createContext("/api/check/", ex -> safe(ex, e -> { CHECK_SLOTS.acquire(); try { handleCheck(e); } finally { CHECK_SLOTS.release(); } }));
        server.createContext("/api/progress", ex -> safe(ex, CourseServer::handleProgress));
        server.createContext("/api/profiles", ex -> safe(ex, CourseServer::handleProfiles));
        server.createContext("/api/code/", ex -> safe(ex, CourseServer::handleCode));
        server.createContext("/api/setup", ex -> safe(ex, CourseServer::handleSetup));
        server.createContext("/", ex -> safe(ex, CourseServer::handleStatic));
        server.setExecutor(Executors.newFixedThreadPool(24));
        server.start();
        System.out.println("Курс запущен: http://localhost:" + port);
    }

    /**
     * Заготовка проекта IntelliJ IDEA для workspace: все папки module-01…module-18 и training сразу
     * отмечены как Sources Root («синие»). Иначе ученик, открыв модуль 2, увидит серую папку без ▶.
     * Создаётся только если IDEA ещё ни разу не открывала эту папку (.idea нет) — чужие настройки не трогаем.
     */
    /** Все папки модулей есть с первого дня (пустые): ученик видит весь курс в IDEA. Заготовки задач кладутся при открытии модуля. */
    private static void ensureModuleFolders() {
        try {
            for (int i = 1; i <= 18; i++) Files.createDirectories(workspaceDir.resolve(String.format("module-%02d", i)));
        } catch (IOException e) {
            System.err.println("Не удалось создать папки модулей: " + e);
        }
    }

    /**
     * «Тренинг код»: для каждой темы tasks/tr-<тема> — папка workspace/trainer/<тема>, помеченная в IDEA как Sources Root.
     * workspace.iml мог уже существовать (проект открыт раньше) — недостающие папки дописываем в него, ничего не удаляя.
     */
    private static void ensureTrainerFolders() {
        try {
            if (!Files.isDirectory(tasksDir)) return;
            List<String> topics = new ArrayList<>();
            try (Stream<Path> dirs = Files.list(tasksDir)) {
                dirs.filter(Files::isDirectory).map(p -> p.getFileName().toString())
                        .filter(n -> n.matches("tr-[a-z]+")).sorted().forEach(n -> topics.add(n.substring(3)));
            }
            for (String t : topics) Files.createDirectories(workspaceDir.resolve("trainer").resolve(t));
            Path iml = workspaceDir.resolve("workspace.iml");
            if (!Files.exists(iml) || topics.isEmpty()) return;
            String xml = Files.readString(iml, StandardCharsets.UTF_8);
            StringBuilder add = new StringBuilder();
            for (String t : topics) {
                String url = "file://$MODULE_DIR$/trainer/" + t;
                if (!xml.contains("\"" + url + "\"")) add.append("      <sourceFolder url=\"").append(url).append("\" isTestSource=\"false\" />\n");
            }
            int at = xml.indexOf("    </content>");
            if (add.length() > 0 && at > 0) {
                Files.writeString(iml, xml.substring(0, at) + add + xml.substring(at), StandardCharsets.UTF_8);
                System.out.println("В проект IDEA добавлены папки «Тренинг код»: " + topics);
            }
        } catch (IOException e) {
            System.err.println("Не удалось подготовить папки «Тренинг код»: " + e);
        }
    }

    /**
     * GET /api/setup — проверка подготовки (урок 0.3): где на компьютере ученика лежит workspace,
     * открыла ли её IntelliJ IDEA (IDEA создаёт .idea/workspace.xml) и какой JDK выбран (misc.xml, project-jdk-name).
     */
    private static void handleSetup(HttpExchange ex) throws IOException {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("hostPath", hostWorkspacePath());
        Path idea = workspaceDir.resolve(".idea");
        res.put("ideaOpened", Files.exists(idea.resolve("workspace.xml")));
        String jdk = null;
        try {
            Matcher m = Pattern.compile("project-jdk-name=\"([^\"]+)\"")
                    .matcher(Files.readString(idea.resolve("misc.xml"), StandardCharsets.UTF_8));
            if (m.find()) jdk = m.group(1);
        } catch (IOException ignored) {
            // нет misc.xml — JDK не выбран
        }
        res.put("jdk", jdk);
        int folders = 0;
        for (int i = 1; i <= 18; i++) if (Files.isDirectory(workspaceDir.resolve(String.format("module-%02d", i)))) folders++;
        res.put("moduleFolders", folders);
        sendJson(ex, 200, res);
    }

    /**
     * Путь к workspace на компьютере ученика. Сервер работает в Docker и видит папку как /workspace,
     * а откуда она подключена, записано в /proc/self/mountinfo. Docker Desktop на Windows:
     * поле root = /Users/V/IdeaProjects/java-course/workspace, источник = C:\ (в файле — "C:\134") →
     * C:\Users\V\IdeaProjects\java-course\workspace. На macOS root = /host_mnt/Users/… → /Users/…
     * Можно задать явно переменной HOST_WORKSPACE_DIR. Не получилось — null: урок покажет, как найти папку вручную.
     */
    static String hostWorkspacePath() {
        String fromEnv = System.getenv("HOST_WORKSPACE_DIR");
        if (fromEnv != null && !fromEnv.isBlank()) return fromEnv;
        try {
            for (String line : Files.readAllLines(Path.of("/proc/self/mountinfo"))) {
                String[] halves = line.split(" - ", 2);
                String[] f = halves[0].split(" ");
                if (halves.length < 2 || f.length < 5 || !f[4].equals(workspaceDir.toString())) continue;
                String root = f[3].replace("\\040", " ");
                String[] tail = halves[1].split(" ");
                String source = tail.length > 1 ? tail[1].replace("\\134", "\\") : "";
                Matcher drive = Pattern.compile("^([A-Za-z]):\\\\?$").matcher(source);
                if (drive.find()) return drive.group(1).toUpperCase() + ":" + root.replace('/', '\\');
                Matcher wsl = Pattern.compile("^/(?:run/desktop/)?mnt/host/([a-z])(/.*)$").matcher(root);
                if (wsl.find()) return wsl.group(1).toUpperCase() + ":" + wsl.group(2).replace('/', '\\');
                if (root.startsWith("/host_mnt/")) return root.substring("/host_mnt".length());
                return root.startsWith("/Users/") || root.startsWith("/home/") ? root : null;
            }
        } catch (Exception ignored) {
            // не Linux-контейнер или нет доступа — путь покажем инструкцией
        }
        return null;
    }

    private static void ensureIdeaProject() {
        try {
            Path idea = workspaceDir.resolve(".idea");
            Files.createDirectories(workspaceDir.resolve("training"));
            if (Files.exists(idea)) return;
            Files.createDirectories(idea);
            StringBuilder roots = new StringBuilder();
            for (int i = 1; i <= 18; i++) {
                roots.append(String.format("      <sourceFolder url=\"file://$MODULE_DIR$/module-%02d\" isTestSource=\"false\" />%n", i));
            }
            roots.append("      <sourceFolder url=\"file://$MODULE_DIR$/training\" isTestSource=\"false\" />\n");
            Files.writeString(workspaceDir.resolve("workspace.iml"), """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <module type="JAVA_MODULE" version="4">
                      <component name="NewModuleRootManager" inherit-compiler-output="true">
                        <exclude-output />
                        <content url="file://$MODULE_DIR$">
                    %s      <excludeFolder url="file://$MODULE_DIR$/.progress" />
                        </content>
                        <orderEntry type="inheritedJdk" />
                        <orderEntry type="sourceFolder" forTests="false" />
                      </component>
                    </module>
                    """.formatted(roots), StandardCharsets.UTF_8);
            Files.writeString(idea.resolve("modules.xml"), """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project version="4">
                      <component name="ProjectModuleManager">
                        <modules>
                          <module fileurl="file://$PROJECT_DIR$/workspace.iml" filepath="$PROJECT_DIR$/workspace.iml" />
                        </modules>
                      </component>
                    </project>
                    """, StandardCharsets.UTF_8);
            Files.writeString(idea.resolve("misc.xml"), """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project version="4">
                      <component name="ProjectRootManager" version="2" languageLevel="JDK_21">
                        <output url="file://$PROJECT_DIR$/out" />
                      </component>
                    </project>
                    """, StandardCharsets.UTF_8);
            System.out.println("Создана заготовка проекта IDEA в workspace (папки модулей уже Sources Root)");
        } catch (IOException e) {
            System.err.println("Не удалось создать заготовку проекта IDEA: " + e);
        }
    }

    private static String env(String name, String def) {
        String v = System.getenv(name);
        return v == null || v.isBlank() ? def : v;
    }

    // ===================================================================
    // Модель задачи
    // ===================================================================

    /** Один тест: что подать на вход и что ожидать на выходе (точно или по регулярке). */
    static class TestCase {
        final String name;
        String input = "";
        String expect;
        String expectRegex;
        String show;        // что показать ученику как «Ожидалось», если проверка по регулярке

        TestCase(String name) {
            this.name = name;
        }
    }

    /** Правило по исходнику: «должно быть» (require) или «не должно быть» (forbid). */
    record Rule(Pattern pattern, String message) {
    }

    static class Task {
        String id;
        String module;
        String title;
        String file;        // путь внутри workspace, например module-01/HelloWorld.java (или внутри проекта, если project)
        boolean project;    // ★-задание сквозного проекта МиниБанк: файл ищем в папке проекта, компилируем весь src
        String className;
        String level = "1";
        String category = "";  // «Тренинг код»: категория внутри темы
        String mode = "output";   // output — сравнить вывод; junit — запустить тесты ученика; pilot — игра «Пилот»
        String map;               // pilot: путь к файлу уровня внутри tasks/
        String seeds;             // pilot: номера карт через запятую
        String description = "";
        final List<TestCase> tests = new ArrayList<>();
        final List<Rule> require = new ArrayList<>();
        final List<Rule> forbid = new ArrayList<>();
        int maxLines;             // «Сократи код»: предел строк кода в теле main (0 — без предела)
        String maxLinesMessage;
    }

    /**
     * Формат файла задачи (*.task):
     * <pre>
     * ## id: m01-t01            ← метаданные
     * @@ description            ← HTML-описание до следующей @@
     * @@ test: Название теста
     * @@ input                  ← что подать в System.in (необязательно)
     * @@ expect                 ← точный ожидаемый вывод
     * @@ expect-regex           ← или регулярное выражение для всего вывода
     * @@ require: регулярка | сообщение, если не найдено
     * @@ forbid: регулярка | сообщение, если найдено
     * </pre>
     */
    static Task parseTask(Path path) throws IOException {
        Task task = new Task();
        task.module = path.getParent().getFileName().toString();
        String section = null;
        StringBuilder buf = new StringBuilder();
        TestCase current = null;

        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            if (line.startsWith("@@ ")) {
                flush(task, current, section, buf);
                section = null;
                buf = new StringBuilder();
                String head = line.substring(3).trim();
                String name = head;
                String arg = "";
                int colon = head.indexOf(':');
                if (colon >= 0) {
                    name = head.substring(0, colon).trim();
                    arg = head.substring(colon + 1).trim();
                }
                switch (name) {
                    case "test" -> {
                        current = new TestCase(arg);
                        task.tests.add(current);
                    }
                    case "require" -> task.require.add(parseRule(arg));
                    case "forbid" -> task.forbid.add(parseRule(arg));
                    // «Сократи код»: в main должно остаться не больше N строк (пустые и комментарии не считаем)
                    case "max-lines" -> {
                        String[] p = arg.split("\\|", 2);
                        task.maxLines = Integer.parseInt(p[0].trim());
                        if (p.length > 1) task.maxLinesMessage = p[1].trim();
                    }
                    default -> section = name;
                }
            } else if (section == null && line.startsWith("## ")) {
                String meta = line.substring(3);
                int colon = meta.indexOf(':');
                if (colon < 0) continue;
                String key = meta.substring(0, colon).trim();
                String value = meta.substring(colon + 1).trim();
                switch (key) {
                    case "id" -> task.id = value;
                    case "title" -> task.title = value;
                    case "file" -> task.file = value;
                    case "class" -> task.className = value;
                    case "level" -> task.level = value;
                    case "category" -> task.category = value;
                    case "mode" -> task.mode = value;
                    case "map" -> task.map = value;
                    case "seeds" -> task.seeds = value;
                    case "project" -> task.project = !value.isEmpty() && !value.equals("no");
                    default -> { /* неизвестные ключи игнорируем */ }
                }
            } else if (section != null) {
                buf.append(line).append('\n');
            }
        }
        flush(task, current, section, buf);
        return task;
    }

    private static void flush(Task task, TestCase test, String section, StringBuilder buf) {
        if (section == null) return;
        String text = buf.toString();
        switch (section) {
            case "description" -> task.description = text.strip();
            case "input" -> { if (test != null) test.input = text; }
            case "expect" -> { if (test != null) test.expect = stripTrailingNewlines(text); }
            case "expect-regex" -> { if (test != null) test.expectRegex = text.strip(); }
            case "show" -> { if (test != null) test.show = text.strip(); }
            default -> { /* неизвестные секции игнорируем */ }
        }
    }

    private static Rule parseRule(String arg) {
        int sep = arg.indexOf(" | ");
        String regex = sep >= 0 ? arg.substring(0, sep) : arg;
        String message = sep >= 0 ? arg.substring(sep + 3) : "Не выполнено требование: " + regex;
        return new Rule(Pattern.compile(regex), message);
    }

    /** Задачи читаются с диска при каждом запросе — можно править файлы, не перезапуская сервер. */
    static List<Task> loadTasks() throws IOException {
        if (!Files.isDirectory(tasksDir)) return List.of();
        List<Path> files;
        try (Stream<Path> walk = Files.walk(tasksDir)) {
            files = walk.filter(p -> p.toString().endsWith(".task")).sorted().toList();
        }
        List<Task> tasks = new ArrayList<>();
        for (Path f : files) {
            try {
                Task t = parseTask(f);
                if (t.id != null) tasks.add(t);
            } catch (RuntimeException e) {
                // одна сломанная задача (например, битая регулярка) не должна ломать все остальные
                System.err.println("Задача пропущена, файл с ошибкой: " + tasksDir.relativize(f) + " — " + e.getMessage());
            }
        }
        return tasks;
    }

    // ===================================================================
    // HTTP-обработчики
    // ===================================================================

    interface Handler {
        void handle(HttpExchange ex) throws Exception;
    }

    private static void safe(HttpExchange ex, Handler h) {
        try {
            h.handle(ex);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                sendJson(ex, 500, Map.of("status", "server_error", "message", "Ошибка сервера: " + e));
            } catch (IOException ignored) {
                // клиент уже ушёл
            }
        } finally {
            ex.close();
        }
    }

    /**
     * Заготовки задач (например, сломанный FixMe.java) лежат в starter/ и копируются в workspace,
     * только если такого файла там ещё нет. Код ученика никогда не перезаписывается.
     */
    static void copyStarters() {
        copyStarters(null);
    }

    /**
     * Копирует заготовки только для одной папки модуля (например, "module-04"), а null — для всех.
     * Заготовки модуля появляются, когда ученик впервые открывает его задачи: так файлы будущих модулей
     * (например, тесты с JUnit, который ещё не подключён) не мешают ему в IDEA раньше времени.
     */
    static void copyStarters(String moduleFolder) {
        Path from = moduleFolder == null ? starterDir : starterDir.resolve(moduleFolder);
        if (!Files.isDirectory(from)) return;
        try (Stream<Path> walk = Files.walk(from)) {
            for (Path src : walk.filter(Files::isRegularFile).toList()) {
                Path dst = workspaceDir.resolve(starterDir.relativize(src).toString()).normalize();
                if (!dst.startsWith(workspaceDir) || Files.exists(dst)) continue;
                Files.createDirectories(dst.getParent());
                Files.copy(src, dst);
                System.out.println("Заготовка добавлена: " + workspaceDir.relativize(dst));
            }
        } catch (IOException e) {
            System.err.println("Не удалось скопировать заготовки: " + e);
        }
    }

    /** GET /api/progress — прогресс ученика; POST — слить присланный прогресс с сохранённым (только прибавление). */
    private static void handleProgress(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        ProgressStore store = storeFor(queryParam(ex, "profile"));
        if ("GET".equals(method)) {
            sendJson(ex, 200, store.load());
            return;
        }
        if ("POST".equals(method) || "PUT".equals(method)) {
            String body = new String(ex.getRequestBody().readNBytes(600_000), StandardCharsets.UTF_8);
            try {
                sendJson(ex, 200, store.mergeAndSave(body));
            } catch (IllegalArgumentException e) {
                sendJson(ex, 400, Map.of("status", "bad_request", "message", e.getMessage()));
            }
            return;
        }
        sendJson(ex, 405, Map.of("status", "method_not_allowed"));
    }

    /** GET /api/tasks?module=m01 — список задач с описаниями (без ожидаемых ответов в явном виде). */
    private static void handleTasks(HttpExchange ex) throws IOException {
        String module = queryParam(ex, "module");
        if (module != null && module.matches("m\\d\\d")) copyStarters("module-" + module.substring(1));
        if (module != null && module.matches("tr-[a-z]+")) copyStarters("trainer/" + module.substring(3)); // «Тренинг код»
        List<Object> out = new ArrayList<>();
        for (Task t : loadTasks()) {
            if (module != null && !module.equals(t.module)) continue;
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", t.id);
            m.put("module", t.module);
            m.put("title", t.title);
            m.put("file", shownPath(t));
            m.put("className", t.className);
            m.put("level", t.level);
            m.put("category", t.category);
            m.put("mode", t.mode);
            m.put("description", t.description);
            m.put("tests", t.tests.stream().map(tc -> tc.name).toList());
            m.put("exists", Files.exists(baseDir(t).resolve(t.file)));
            out.add(m);
        }
        sendJson(ex, 200, out);
    }

    /** GET /api/status — последний результат проверки по каждой задаче (из workspace/.history.log). */
    private static void handleStatus(HttpExchange ex) throws IOException {
        Map<String, Object> last = new LinkedHashMap<>();
        Path log = workspaceDir.resolve(".history.log");
        String profile = validProfile(queryParam(ex, "profile"));
        String legacyOwner = profile == null ? null : String.valueOf(readProfiles().getOrDefault("legacyOwner", ""));
        if (Files.exists(log)) {
            for (String line : Files.readAllLines(log, StandardCharsets.UTF_8)) {
                String[] parts = line.split(" \\| ");
                // последнее поле «profile=…» — чья проверка; строки без него — старые, до профилей
                String owner = parts.length >= 5 && parts[parts.length - 1].startsWith("profile=") ? parts[parts.length - 1].substring(8).trim() : null;
                if (profile != null && !(profile.equals(owner) || (owner == null && profile.equals(legacyOwner)))) continue;
                if (parts.length >= 3) {
                    last.put(parts[1].trim(), Map.of("time", parts[0].trim(), "status", parts[2].trim()));
                }
            }
        }
        sendJson(ex, 200, last);
    }

    /**
     * GET /api/code/{id} — текст файла ученика для задачи (или заготовка из starter/), POST — сохранить его.
     * Только для задач-игр, где код пишут прямо на странице урока («Раннер»). Файл ложится туда же,
     * где его ждёт обычная проверка, — workspace/<file>, так что открыть его можно и в IDEA.
     */
    private static void handleCode(HttpExchange ex) throws Exception {
        String id = ex.getRequestURI().getPath().substring("/api/code/".length());
        Task task = loadTasks().stream().filter(t -> t.id.equals(id)).findFirst().orElse(null);
        if (task == null || !"runner".equals(task.mode)) {
            sendJson(ex, 404, Map.of("status", "not_found", "message", "Для этой задачи код на странице не пишут: " + id));
            return;
        }
        Path file = workspaceDir.resolve(task.file).normalize();
        if (!file.startsWith(workspaceDir)) { sendJson(ex, 400, Map.of("status", "bad_path")); return; }
        if ("POST".equals(ex.getRequestMethod())) {
            byte[] body = ex.getRequestBody().readNBytes(64 * 1024 + 1);
            if (body.length > 64 * 1024) { sendJson(ex, 413, Map.of("status", "too_big", "message", "Слишком длинный код (больше 64 КБ).")); return; }
            Files.createDirectories(file.getParent());
            Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
            Files.write(tmp, body);
            Files.move(tmp, file, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            sendJson(ex, 200, Map.of("status", "saved", "file", "workspace/" + task.file));
            return;
        }
        Path starter = starterDir.resolve(task.file).normalize();
        String code = Files.exists(file) ? Files.readString(file, StandardCharsets.UTF_8)
                : Files.exists(starter) ? Files.readString(starter, StandardCharsets.UTF_8) : "";
        sendJson(ex, 200, Map.of("code", code, "saved", Files.exists(file), "file", "workspace/" + task.file));
    }

    // ===================================================================
    // Профили: у каждого свой прогресс уроков, история проверок и режим курса
    // ===================================================================
    private static final Map<String, ProgressStore> STORES = new java.util.concurrent.ConcurrentHashMap<>();

    static String validProfile(String id) {
        return id != null && id.matches("[a-z0-9-]{1,40}") ? id : null;
    }

    static ProgressStore storeFor(String profileId) {
        String id = validProfile(profileId);
        if (id == null) return progress;
        return STORES.computeIfAbsent(id, k -> new ProgressStore(workspaceDir.resolve(".progress").resolve("profiles").resolve(k)));
    }

    @SuppressWarnings("unchecked")
    static synchronized Map<String, Object> readProfiles() {
        Path f = workspaceDir.resolve(".progress").resolve("profiles.json");
        try {
            if (Files.exists(f)) {
                Object o = Json.parse(Files.readString(f, StandardCharsets.UTF_8));
                if (o instanceof Map<?, ?> m) return new LinkedHashMap<>((Map<String, Object>) m);
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("profiles.json не читается: " + e);
        }
        Map<String, Object> empty = new LinkedHashMap<>();
        empty.put("profiles", new ArrayList<>());
        return empty;
    }

    static synchronized void writeProfiles(Map<String, Object> data) throws IOException {
        Path dir = Files.createDirectories(workspaceDir.resolve(".progress"));
        Path tmp = dir.resolve("profiles.json.tmp");
        Files.writeString(tmp, Json.stringify(data), StandardCharsets.UTF_8);
        Files.move(tmp, dir.resolve("profiles.json"), java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                java.nio.file.StandardCopyOption.ATOMIC_MOVE);
    }

    /**
     * GET /api/profiles — список профилей.
     * POST /api/profiles {name, mode, tool} — создать (mode: ai — с наставником Claude Code, free — без подписки).
     * POST /api/profiles/{id} {name?, mode?, tool?} — изменить.
     */
    @SuppressWarnings("unchecked")
    private static synchronized void handleProfiles(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        Map<String, Object> reg = readProfiles();
        List<Object> list = new ArrayList<>((List<Object>) reg.getOrDefault("profiles", new ArrayList<>()));
        reg.put("profiles", list);
        if ("GET".equals(ex.getRequestMethod())) {
            sendJson(ex, 200, list);
            return;
        }
        if (!"POST".equals(ex.getRequestMethod())) {
            sendJson(ex, 405, Map.of("status", "method_not_allowed"));
            return;
        }
        Object parsed = Json.parse(new String(ex.getRequestBody().readNBytes(8192), StandardCharsets.UTF_8));
        if (!(parsed instanceof Map<?, ?> in)) {
            sendJson(ex, 400, Map.of("status", "bad_request"));
            return;
        }
        String mode = "free".equals(in.get("mode")) ? "free" : "ai";
        String tool = in.get("tool") == null ? "" : String.valueOf(in.get("tool")).replaceAll("[^a-z]", "");
        String name = in.get("name") == null ? "" : String.valueOf(in.get("name")).strip();
        if (name.length() > 40) name = name.substring(0, 40);
        String idPart = path.length() > "/api/profiles/".length() ? validProfile(path.substring("/api/profiles/".length())) : null;
        if (idPart != null) { // изменить существующий профиль
            for (Object o : list) {
                Map<String, Object> p = (Map<String, Object>) o;
                if (idPart.equals(p.get("id"))) {
                    if (!name.isEmpty()) p.put("name", name);
                    if (in.containsKey("mode")) p.put("mode", mode);
                    if (in.containsKey("tool")) p.put("tool", tool);
                    writeProfiles(reg);
                    sendJson(ex, 200, p);
                    return;
                }
            }
            sendJson(ex, 404, Map.of("status", "not_found"));
            return;
        }
        if (name.isEmpty()) {
            sendJson(ex, 400, Map.of("status", "bad_request", "message", "Нужно имя"));
            return;
        }
        String id = "p" + Long.toString(System.currentTimeMillis(), 36);
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("id", id);
        p.put("name", name);
        p.put("mode", mode);
        p.put("tool", tool);
        p.put("created", LocalDateTime.now().format(TIME));
        // первый профиль забирает прогресс, накопленный до появления профилей
        if (list.isEmpty() && progress.exists()) {
            storeFor(id).mergeAndSave(Json.stringify(progress.load()));
            reg.put("legacyOwner", id);
        }
        list.add(p);
        writeProfiles(reg);
        sendJson(ex, 200, p);
    }

    /** POST /api/check/{id} — проверить решение. */
    static final java.util.concurrent.Semaphore CHECK_SLOTS = new java.util.concurrent.Semaphore(4, true);

    private static void handleCheck(HttpExchange ex) throws Exception {
        String id = ex.getRequestURI().getPath().substring("/api/check/".length());
        Task task = loadTasks().stream().filter(t -> t.id.equals(id)).findFirst().orElse(null);
        if (task == null) {
            sendJson(ex, 404, Map.of("status", "not_found", "message", "Нет такой задачи: " + id));
            return;
        }
        if (!task.project && task.file.contains("/")) copyStarters(task.file.substring(0, task.file.indexOf('/')));
        Map<String, Object> result = check(task);
        appendHistory(task, result, validProfile(queryParam(ex, "profile")));
        sendJson(ex, 200, result);
    }

    private static void handleStatic(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        if (path.endsWith("/")) path += "index.html";
        Path file = siteDir.resolve(path.substring(1)).normalize();
        if (!file.startsWith(siteDir) || !Files.isRegularFile(file)) {
            byte[] body = "<h1>404</h1><p>Такой страницы нет. <a href=\"/\">На главную</a></p>".getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            ex.sendResponseHeaders(404, body.length);
            ex.getResponseBody().write(body);
            return;
        }
        byte[] body = Files.readAllBytes(file);
        ex.getResponseHeaders().set("Content-Type", contentType(file.toString()));
        ex.getResponseHeaders().set("Cache-Control", "no-store");
        ex.sendResponseHeaders(200, body.length);
        ex.getResponseBody().write(body);
    }

    private static String contentType(String name) {
        String n = name.toLowerCase();
        if (n.endsWith(".html")) return "text/html; charset=utf-8";
        if (n.endsWith(".css")) return "text/css; charset=utf-8";
        if (n.endsWith(".js")) return "text/javascript; charset=utf-8";
        if (n.endsWith(".json")) return "application/json; charset=utf-8";
        if (n.endsWith(".svg")) return "image/svg+xml";
        if (n.endsWith(".png")) return "image/png";
        if (n.endsWith(".jpg") || n.endsWith(".jpeg")) return "image/jpeg";
        if (n.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }

    private static String queryParam(HttpExchange ex, String name) {
        String q = ex.getRequestURI().getQuery();
        if (q == null) return null;
        for (String pair : q.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv[0].equals(name)) return kv.length > 1 ? kv[1] : "";
        }
        return null;
    }

    // ===================================================================
    // Проверка решения
    // ===================================================================

    /** Где лежит файл задачи: обычные задачи — в workspace, ★-задания — в папке проекта МиниБанк. */
    static Path baseDir(Task t) { return t.project ? projectDir : workspaceDir; }

    /** Путь для сообщений ученику. */
    static String shownPath(Task t) { return (t.project ? "MiniBank/" : "workspace/") + t.file; }

    static Map<String, Object> check(Task task) throws Exception {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("id", task.id);
        res.put("file", shownPath(task));
        Path base = baseDir(task);
        Path source = base.resolve(task.file).normalize();

        if (!source.startsWith(base) || !Files.exists(source)) {
            res.put("status", "missing");
            res.put("message", task.project
                    ? "Не найден файл MiniBank/" + task.file + ". Проект МиниБанк должен лежать в папке курса, в папке MiniBank (как в ★-задании модуля 1)."
                    : "Файл не найден: workspace/" + task.file
                    + ". Создай его (имя файла должно совпадать с именем класса " + task.className + ")."
                    + " Файл уже есть, а сайт его не видит? Скорее всего, в IDEA открыт другой проект — открой папку workspace"
                    + " из папки курса (урок 0.3 «Подготовка»).");
            return res;
        }

        String code = Files.readString(source, StandardCharsets.UTF_8);
        List<String> hints = new ArrayList<>();
        boolean manual = task.tests.isEmpty();

        Path tmp = Files.createTempDirectory("check-");
        try {
            Path src = tmp.resolve(source.getFileName());
            Files.writeString(src, code, StandardCharsets.UTF_8);
            Path classes = Files.createDirectories(tmp.resolve("out"));

            // 1. Компиляция (с библиотеками JUnit/AssertJ на classpath, если они есть в образе)
            List<String> javac = new ArrayList<>(List.of("javac", "-encoding", "UTF-8", "-nowarn", "-d", classes.toString()));
            if (libClasspath() != null) { javac.add("-cp"); javac.add(libClasspath()); }
            if (task.project) { // весь проект: все классы из src (с модуля 9 их станет много)
                try (Stream<Path> all = Files.walk(base.resolve("src"))) {
                    all.filter(p -> p.toString().endsWith(".java")).forEach(p -> javac.add(p.toString()));
                }
            } else {
                javac.add(src.toString());
            }
            if (isGame(task)) { // игры «Пилот» и «Раннер»: компилируем вместе с движком (tasks/<режим>/engine)
                try (Stream<Path> eng = Files.list(tasksDir.resolve(task.mode).resolve("engine"))) {
                    eng.filter(p -> p.toString().endsWith(".java")).forEach(p -> javac.add(p.toString()));
                }
            }
            ProcessResult compile = run(javac, "", COMPILE_TIMEOUT_SEC);
            if (compile.exitCode != 0) {
                String output = (compile.stdout + compile.stderr).replace(src.toString(), shownPath(task))
                        .replace(projectDir.toString() + File.separator, "MiniBank/").replace(projectDir.toString() + "/", "MiniBank/");
                res.put("status", "compile_error");
                res.put("message", "Программа не компилируется — javac нашёл ошибки. Начни с первой.");
                res.put("compilerOutput", output.strip());
                hints.addAll(explainCompileErrors(output, task));
                res.put("hints", hints);
                return res;
            }

            // 2. Правила по исходнику (комментарии не учитываем)
            String codeNoComments = stripComments(code);
            List<String> ruleFails = new ArrayList<>();
            for (Rule r : task.require) {
                if (!r.pattern().matcher(codeNoComments).find()) ruleFails.add(r.message());
            }
            for (Rule r : task.forbid) {
                if (r.pattern().matcher(codeNoComments).find()) ruleFails.add(r.message());
            }
            if (task.maxLines > 0) {
                int n = countMainLines(codeNoComments);
                if (n > task.maxLines) ruleFails.add((task.maxLinesMessage != null ? task.maxLinesMessage + " " : "")
                        + "Сейчас в main " + n + " строк кода, нужно не больше " + task.maxLines + ".");
            }

            // 2б. Режим JUnit: запускаем тесты ученика консольным запускателем JUnit 5
            if ("junit".equals(task.mode)) return checkJunit(task, res, classes, ruleFails);
            // 2в. Игра «Пилот»: прогоняем полёт на нескольких картах
            if (isGame(task)) return checkGame(task, res, classes, ruleFails);

            // 3. Запуск тестов
            List<Object> tests = new ArrayList<>();
            int passed = 0;
            String runCp = classes + (libClasspath() != null ? File.pathSeparator + libClasspath() : "");
            for (TestCase tc : task.tests) {
                ProcessResult run = run(List.of("java", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8",
                        "-Dstderr.encoding=UTF-8", "-Xshare:auto", "-cp", runCp, task.className), tc.input, RUN_TIMEOUT_SEC);
                Map<String, Object> t = new LinkedHashMap<>();
                t.put("name", tc.name);
                if (!tc.input.isEmpty()) t.put("input", tc.input.strip());
                String actual = normalize(run.stdout);
                t.put("actual", actual);
                boolean ok;
                if (run.timedOut) {
                    ok = false;
                    t.put("error", "Программа работала дольше " + RUN_TIMEOUT_SEC + " секунд и была остановлена. "
                            + "Возможно, она ждёт ввода с клавиатуры или застряла в бесконечном цикле.");
                } else if (run.exitCode != 0) {
                    ok = false;
                    t.put("error", explainRuntimeError(run.stderr, task));
                    t.put("stderr", run.stderr.strip());
                } else if (tc.expectRegex != null) {
                    ok = Pattern.compile(tc.expectRegex, Pattern.DOTALL).matcher(actual).matches();
                    t.put("expected", tc.show != null ? tc.show : "(любой вывод подходящего вида — см. условие)");
                } else {
                    String expected = normalize(tc.expect == null ? "" : tc.expect);
                    ok = expected.equals(actual);
                    t.put("expected", expected);
                    if (!ok) {
                        String diff = describeDifference(expected, actual);
                        if (diff != null) t.put("diff", diff);
                    }
                }
                t.put("passed", ok);
                if (ok) passed++;
                tests.add(t);
            }

            if (manual) {
                // задачу без автотестов (JUnit, Selenide, «объясни словами») проверяет наставник
                res.put("status", ruleFails.isEmpty() ? "manual" : "failed");
                res.put("ruleFails", ruleFails);
                res.put("message", ruleFails.isEmpty()
                        ? "Код компилируется. Эту задачу проверяет наставник: напиши в чате /check " + task.id
                        : "Код компилируется, но пока не всё по условию.");
                return res;
            }
            boolean allOk = passed == task.tests.size() && ruleFails.isEmpty();
            res.put("status", allOk ? "passed" : "failed");
            res.put("passedTests", passed);
            res.put("totalTests", task.tests.size());
            res.put("tests", tests);
            res.put("ruleFails", ruleFails);
            res.put("message", allOk
                    ? "Отлично! Все проверки пройдены. Покажи решение наставнику: /check " + task.id
                    : "Пока не всё сходится. Посмотри, чем отличается твой вывод от ожидаемого.");
            return res;
        } finally {
            deleteRecursively(tmp);
        }
    }

    record ProcessResult(int exitCode, String stdout, String stderr, boolean timedOut) {
    }

    /** Папка с библиотеками (JUnit 5, AssertJ) → строка classpath «папка/*» или null, если библиотек нет. */
    private static String libClasspath() {
        Path lib = Path.of(env("LIB_DIR", "lib")).toAbsolutePath();
        if (!Files.isDirectory(lib)) return null;
        try (Stream<Path> s = Files.list(lib)) {
            if (s.noneMatch(p -> p.toString().endsWith(".jar"))) return null;
        } catch (IOException e) {
            return null;
        }
        return lib + File.separator + "*";
    }

    static boolean isGame(Task task) { return "pilot".equals(task.mode) || "runner".equals(task.mode); }

    /** Игры: код ученика проходит карты уровня («Пилот» — полёт, «Раннер» — забег); задача решена, если прошёл все. */
    private static Map<String, Object> checkGame(Task task, Map<String, Object> res, Path classes, List<String> ruleFails) throws Exception {
        boolean runner = "runner".equals(task.mode);
        String main = runner ? "RunnerGame" : "PilotGame";
        Path map = tasksDir.resolve(task.map == null ? task.mode + "/levels/level-1.txt" : task.map).normalize();
        List<Object> tests = new ArrayList<>();
        List<Object> replays = new ArrayList<>();
        int passed = 0;
        String[] seeds = (task.seeds == null ? "1,2,3" : task.seeds).split(",");
        for (int i = 0; i < seeds.length; i++) {
            ProcessResult run = run(List.of("java", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "-cp", classes.toString(),
                    main, map.toString(), seeds[i].strip()), "", RUN_TIMEOUT_SEC + 5);
            Map<String, Object> t = new LinkedHashMap<>();
            t.put("name", (runner ? "Трасса " : "Карта ") + (i + 1));
            Object replay = null;
            if (run.timedOut) {
                t.put("error", (runner ? "Забег" : "Полёт") + " длился слишком долго — похоже, в методе decide бесконечный цикл.");
            } else {
                try {
                    replay = Json.parse(run.stdout.strip());
                } catch (RuntimeException e) {
                    t.put("error", "Не удалось запустить игру: " + (run.stderr.isBlank() ? e.getMessage() : run.stderr.strip()));
                }
            }
            boolean ok = false;
            if (replay instanceof Map<?, ?> rm) {
                ok = "finish".equals(rm.get("result"));
                t.put("actual", String.valueOf(rm.get("message")));
                if (!ok) t.put("error", String.valueOf(rm.get("message")));
                replays.add(replay);
            } else {
                replays.add(null);
            }
            t.put("expected", runner ? "добежать до финиша без столкновений" : "долететь до финиша без аварий");
            t.put("passed", ok);
            if (ok) passed++;
            tests.add(t);
        }
        boolean allOk = passed == seeds.length && ruleFails.isEmpty();
        res.put("status", allOk ? "passed" : "failed");
        res.put("passedTests", passed);
        res.put("totalTests", seeds.length);
        res.put("tests", tests);
        res.put("ruleFails", ruleFails);
        res.put("replays", replays);
        res.put("message", allOk ? (runner ? "Джавик добежал на всех трассах! Покажи код наставнику: /check " : "Корабль долетел на всех картах! Покажи код наставнику: /check ") + task.id
                : runner ? "Не на всех трассах получилось. Посмотри забег по ходам: что показывал радар перед столкновением?"
                : "Не на всех картах получилось. Посмотри полёт по шагам на странице игры.");
        return res;
    }

    /** Задача в режиме junit: ученик пишет тесты, мы запускаем их и смотрим, все ли зелёные. */
    private static Map<String, Object> checkJunit(Task task, Map<String, Object> res, Path classes, List<String> ruleFails) throws Exception {
        String lib = libClasspath();
        if (lib == null) {
            res.put("status", "manual");
            res.put("message", "Библиотеки JUnit не найдены на сервере — эту задачу проверит наставник: /check " + task.id);
            return res;
        }
        Path libDir = Path.of(lib.substring(0, lib.length() - 1));
        Path launcher = libDir.resolve("junit-platform-console-standalone.jar");
        // запускатель JUnit не понимает «папка/*» — перечисляем jar-файлы явно
        StringBuilder cp = new StringBuilder(classes.toString());
        try (Stream<Path> s = Files.list(libDir)) {
            s.filter(p -> p.toString().endsWith(".jar") && !p.equals(launcher)).sorted()
                    .forEach(p -> cp.append(File.pathSeparator).append(p));
        }
        ProcessResult run = run(List.of("java", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "-jar", launcher.toString(),
                "execute", "--class-path", cp.toString(), "--scan-class-path",
                "--disable-banner", "--disable-ansi-colors", "--details=tree", "--fail-if-no-tests"), "", 60);
        String output = normalize(run.stdout + "\n" + run.stderr);
        boolean ok = run.exitCode == 0 && !run.timedOut;
        Map<String, Object> t = new LinkedHashMap<>();
        t.put("name", "Запуск твоих JUnit-тестов");
        t.put("actual", output.length() > 4000 ? output.substring(0, 4000) + "\n…" : output);
        t.put("expected", "все тесты зелёные (✔), ни одного ✘");
        t.put("passed", ok);
        if (!ok) t.put("error", run.timedOut ? "Тесты работали слишком долго." : "Есть упавшие тесты (✘) или тестов не нашлось. Посмотри вывод ниже.");
        boolean allOk = ok && ruleFails.isEmpty();
        res.put("status", allOk ? "passed" : "failed");
        res.put("passedTests", ok ? 1 : 0);
        res.put("totalTests", 1);
        res.put("tests", List.of(t));
        res.put("ruleFails", ruleFails);
        res.put("message", allOk ? "Все тесты зелёные! Покажи решение наставнику: /check " + task.id : "Пока не всё сходится.");
        return res;
    }

    private static ProcessResult run(List<String> cmd, String input, int timeoutSec) throws Exception {
        Path runDir = Files.createTempDirectory("run-");
        Path out = runDir.resolve("stdout.txt");
        Path err = runDir.resolve("stderr.txt");
        Files.createFile(out);
        Files.createFile(err);
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            // программа ученика работает в своей временной папке: если она создаёт файлы, они не мусорят на сервере
            pb.directory(out.getParent().toFile());
            pb.redirectOutput(out.toFile());
            pb.redirectError(err.toFile());
            Process p = pb.start();
            try (OutputStream stdin = p.getOutputStream()) {
                stdin.write(input.getBytes(StandardCharsets.UTF_8));
            } catch (IOException ignored) {
                // программа не читает ввод и уже закрыла поток — это нормально
            }
            boolean finished = p.waitFor(timeoutSec, TimeUnit.SECONDS);
            if (!finished) {
                p.destroyForcibly();
                p.waitFor(2, TimeUnit.SECONDS);
            }
            return new ProcessResult(finished ? p.exitValue() : -1,
                    readLimited(out), readLimited(err), !finished);
        } finally {
            deleteRecursively(runDir);
        }
    }

    /** Читаем не больше 20 КБ — вдруг программа напечатала миллион строк. */
    private static String readLimited(Path p) throws IOException {
        byte[] bytes = Files.readAllBytes(p);
        int limit = 20_000;
        String s = new String(bytes, 0, Math.min(bytes.length, limit), StandardCharsets.UTF_8);
        return bytes.length > limit ? s + "\n…(вывод обрезан)" : s;
    }

    /** Сравниваем без учёта \r\n vs \n, пробелов в конце строк и пустых строк в самом конце. */
    static String normalize(String s) {
        String[] lines = s.replace("\r\n", "\n").replace('\r', '\n').split("\n", -1);
        StringBuilder sb = new StringBuilder();
        for (String line : lines) sb.append(line.stripTrailing()).append('\n');
        return stripTrailingNewlines(sb.toString());
    }

    private static String stripTrailingNewlines(String s) {
        int end = s.length();
        while (end > 0 && (s.charAt(end - 1) == '\n' || s.charAt(end - 1) == '\r')) end--;
        return s.substring(0, end);
    }

    /** Подсказываем, в какой строке и в каком символе впервые расходятся ожидаемый и реальный вывод. */
    private static String describeDifference(String expected, String actual) {
        String[] e = expected.split("\n", -1);
        String[] a = actual.split("\n", -1);
        if (actual.isEmpty()) return "Программа ничего не напечатала.";
        for (int i = 0; i < Math.max(e.length, a.length); i++) {
            if (i >= e.length) return "Лишняя строка " + (i + 1) + ": «" + a[i] + "»";
            if (i >= a.length) return "Не хватает строки " + (i + 1) + ": «" + e[i] + "»";
            if (!e[i].equals(a[i])) {
                int pos = 0;
                while (pos < e[i].length() && pos < a[i].length() && e[i].charAt(pos) == a[i].charAt(pos)) pos++;
                String exp = pos < e[i].length() ? "«" + visible(e[i].charAt(pos)) + "»" : "конец строки";
                String act = pos < a[i].length() ? "«" + visible(a[i].charAt(pos)) + "»" : "конец строки";
                return "Строка " + (i + 1) + ", символ " + (pos + 1) + ": ожидался " + exp + ", а у тебя " + act + ".";
            }
        }
        return null;
    }

    private static String visible(char c) {
        return switch (c) {
            case ' ' -> "пробел";
            case '\t' -> "табуляция \\t";
            default -> String.valueOf(c);
        };
    }

    private static String stripComments(String code) {
        return code.replaceAll("(?s)/\\*.*?\\*/", " ").replaceAll("//[^\\n]*", " ");
    }

    /** Сколько непустых строк кода в теле метода main (для правила max-lines задач «Сократи код»). */
    static int countMainLines(String codeNoComments) {
        Matcher m = Pattern.compile("static\\s+void\\s+main\\s*\\([^)]*\\)\\s*(throws[^{]*)?\\{").matcher(codeNoComments);
        if (!m.find()) return Integer.MAX_VALUE;
        int depth = 1, i = m.end();
        StringBuilder body = new StringBuilder();
        for (; i < codeNoComments.length() && depth > 0; i++) {
            char c = codeNoComments.charAt(i);
            if (c == '{') depth++;
            if (c == '}') depth--;
            if (depth > 0) body.append(c);
        }
        int n = 0;
        for (String line : body.toString().split("\n")) {
            String t = line.trim();
            if (!t.isEmpty() && !t.equals("}") && !t.equals("{")) n++;
        }
        return n;
    }

    // ===================================================================
    // Перевод ошибок на человеческий язык
    // ===================================================================

    private static final Object[][] COMPILE_HINTS = {
            {"';' expected", "«';' expected» — пропущена точка с запятой. Каждая инструкция заканчивается на ;. Посмотри на указанную строку и строку перед ней."},
            {"cannot find symbol", "«cannot find symbol» — Java не знает это имя. Чаще всего это опечатка или неправильный регистр: System, а не system; println, а не printIn."},
            {"should be declared in a file named", "Имя public-класса должно совпадать с именем файла (вплоть до больших и маленьких букв)."},
            {"unclosed string literal", "«unclosed string literal» — строка не закрыта: у текста в кавычках не хватает закрывающей \"."},
            {"reached end of file while parsing", "«reached end of file while parsing» — файл закончился раньше, чем закрылись все блоки. Скорее всего, не хватает закрывающей фигурной скобки }."},
            {"illegal character", "«illegal character» — в коде есть символ, который Java не понимает. Частая причина — «ёлочки» или “кривые” кавычки, скопированные из мессенджера или Word. В коде нужны прямые \"."},
            {"class, interface, enum, or record expected", "«class, interface, enum, or record expected» — код оказался вне класса. Часто это лишняя } или код ниже последней скобки."},
            {"not a statement", "«not a statement» — эта строка не похожа на команду. Возможно, текст забыли взять в кавычки."},
            {"illegal start of expression", "«illegal start of expression» — Java запуталась в начале выражения. Проверь скобки и точку с запятой в этой и предыдущей строке."},
            {"')' expected", "«')' expected» — не хватает закрывающей круглой скобки )."},
            {"illegal escape character", "«illegal escape character» — после \\ в строке стоит символ, который не является спецсимволом. Чтобы напечатать сам \\, пиши \\\\."},
            {"<identifier> expected", "«<identifier> expected» — Java ожидала имя (например, имя метода или переменной). Часто это код, написанный прямо в классе, а не внутри метода main."},
            {"invalid method declaration; return type required", "«return type required» — у метода не указан тип результата. Для main это void: public static void main(String[] args)."},
            {"unclosed comment", "«unclosed comment» — комментарий /* не закрыт символами */."},
            {"incompatible types", "«incompatible types» — значение не того типа. Например, число там, где ждали строку."},
    };

    private static List<String> explainCompileErrors(String output, Task task) {
        List<String> hints = new ArrayList<>();
        for (Object[] h : COMPILE_HINTS) {
            if (output.contains((String) h[0])) hints.add((String) h[1]);
        }
        Matcher m = Pattern.compile("(\\d+) errors?").matcher(output);
        if (m.find() && Integer.parseInt(m.group(1)) > 1) {
            hints.add("Ошибок несколько. Исправь первую и проверь снова — часть остальных часто исчезает сама.");
        }
        if (hints.isEmpty()) {
            hints.add("Прочитай первую строку ошибки: там имя файла, номер строки и описание. Если непонятно — спроси наставника: /hint " + task.id);
        }
        return hints;
    }

    private static String explainRuntimeError(String stderr, Task task) {
        if (stderr.contains("Main method not found") || stderr.contains("main method not found")) {
            return "Java не нашла метод main. Проверь, что он написан ровно так: public static void main(String[] args)";
        }
        if (stderr.contains("Could not find or load main class")) {
            return "Не удалось найти класс " + task.className + ". Проверь имя класса в файле — оно должно быть " + task.className + ".";
        }
        if (stderr.contains("ArithmeticException")) {
            return "Программа упала: деление на ноль (ArithmeticException).";
        }
        if (stderr.contains("Exception in thread")) {
            String first = stderr.lines().filter(l -> l.contains("Exception")).findFirst().orElse("");
            return "Программа упала во время работы: " + first.strip();
        }
        return "Программа завершилась с ошибкой.";
    }

    // ===================================================================
    // История проверок и утилиты
    // ===================================================================

    private static synchronized void appendHistory(Task task, Map<String, Object> result, String profile) {
        try {
            Files.createDirectories(workspaceDir);
            String status = String.valueOf(result.get("status")).toUpperCase();
            String details = result.containsKey("totalTests")
                    ? result.get("passedTests") + "/" + result.get("totalTests") + " тестов"
                    : String.valueOf(result.getOrDefault("message", ""));
            String line = LocalDateTime.now().format(TIME) + " | " + task.id + " | " + status + " | "
                    + details.replace(" | ", " / ").replace("\n", " ") + (profile != null ? " | profile=" + profile : "") + "\n";
            Files.writeString(workspaceDir.resolve(".history.log"), line, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Не удалось записать историю: " + e);
        }
    }

    private static void deleteRecursively(Path dir) {
        try (Stream<Path> walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        } catch (IOException ignored) {
            // временная папка — не страшно, если что-то осталось
        }
    }

    private static void sendJson(HttpExchange ex, int code, Object body) throws IOException {
        byte[] bytes = toJson(body).getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.getResponseHeaders().set("Cache-Control", "no-store");
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
    }

    /** Мини-сериализатор в JSON: Map, List, String, Number, Boolean, null. */
    static String toJson(Object o) {
        if (o == null) return "null";
        if (o instanceof String s) return quote(s);
        if (o instanceof Number || o instanceof Boolean) return o.toString();
        if (o instanceof Map<?, ?> map) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> e : map.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append(quote(String.valueOf(e.getKey()))).append(':').append(toJson(e.getValue()));
            }
            return sb.append('}').toString();
        }
        if (o instanceof Iterable<?> list) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : list) {
                if (!first) sb.append(',');
                first = false;
                sb.append(toJson(item));
            }
            return sb.append(']').toString();
        }
        return quote(o.toString());
    }

    private static String quote(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        return sb.append('"').toString();
    }
}
