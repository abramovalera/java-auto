import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Надёжное хранилище прогресса одного профиля: <папка>/progress.json
 * (профили — workspace/.progress/profiles/<id>/, старый общий прогресс — workspace/.progress/).
 * <ul>
 *   <li><b>Только прибавление.</b> Новые данные не заменяют старые, а сливаются с ними:
 *       списки объединяются, числа берутся максимальные. Пустой браузер не может стереть прогресс.</li>
 *   <li><b>Атомарная запись.</b> Сначала во временный файл, потом подмена — полузаписанного файла не бывает.</li>
 *   <li><b>Резервные копии.</b> progress.prev.json — предыдущая версия, backups/progress-ГГГГ-ММ-ДД.json — снимок
 *       за каждый день (последние 30).</li>
 *   <li><b>Самовосстановление.</b> Повреждённый файл откладывается в сторону (не затирается),
 *       данные берутся из предыдущей версии или последнего снимка.</li>
 * </ul>
 */
public class ProgressStore {

    /** Какие ключи считаются прогрессом. Настройки внешнего вида (тема, свёрнутые модули) — нет. */
    static final Pattern KEY = Pattern.compile("^(jc\\.(read|awards|days|xp|step\\.[\\w.-]{1,40})|w:[^\\s]{1,200})$");
    private static final int MAX_BODY = 512 * 1024;
    private static final int KEEP_DAILY = 30;
    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final Path dir;
    private final Path file;
    private final Path prev;
    private final Path backups;

    /** dir — папка, где лежат progress.json, progress.prev.json и backups/. */
    public ProgressStore(Path dir) {
        this.dir = dir;
        this.file = dir.resolve("progress.json");
        this.prev = dir.resolve("progress.prev.json");
        this.backups = dir.resolve("backups");
    }

    /** Есть ли уже сохранённый прогресс в этой папке. */
    public boolean exists() {
        return Files.exists(file);
    }

    /** Текущий прогресс. Никогда не бросает исключение из-за повреждённого файла. */
    public synchronized Map<String, Object> load() {
        Map<String, Object> data = readMap(file);
        if (data != null) return data;
        if (Files.exists(file)) quarantine(file);
        // основной файл пуст или повреждён — пробуем предыдущую версию, затем свежий дневной снимок
        data = readMap(prev);
        if (data != null) return data;
        for (Path b : listBackups(true)) {
            data = readMap(b);
            if (data != null) return data;
        }
        return new LinkedHashMap<>();
    }

    /** Слить пришедшие данные с сохранёнными и записать. Возвращает итоговый прогресс. */
    public synchronized Map<String, Object> mergeAndSave(String body) throws IOException {
        if (body.length() > MAX_BODY) throw new IllegalArgumentException("Слишком большой прогресс: " + body.length() + " байт");
        Object parsed = Json.parse(body);
        if (!(parsed instanceof Map<?, ?> incomingRaw)) throw new IllegalArgumentException("Ожидался JSON-объект");

        Map<String, Object> incoming = new LinkedHashMap<>();
        for (Map.Entry<?, ?> e : incomingRaw.entrySet()) {
            String k = String.valueOf(e.getKey());
            if (KEY.matcher(k).matches() && e.getValue() != null) incoming.put(k, e.getValue());
        }

        Map<String, Object> current = load();
        @SuppressWarnings("unchecked")
        Map<String, Object> merged = (Map<String, Object>) merge(current, incoming);
        if (Json.stringify(merged).equals(Json.stringify(current)) && Files.exists(file)) {
            return merged; // ничего нового — диск не трогаем
        }
        write(merged);
        return merged;
    }

    /** Слияние «только прибавление». */
    static Object merge(Object a, Object b) {
        if (a == null) return b;
        if (b == null) return a;
        if (a instanceof Map<?, ?> ma && b instanceof Map<?, ?> mb) {
            Map<String, Object> r = new LinkedHashMap<>();
            ma.forEach((k, v) -> r.put(String.valueOf(k), v));
            mb.forEach((k, v) -> r.put(String.valueOf(k), merge(r.get(String.valueOf(k)), v)));
            return r;
        }
        if (a instanceof List<?> la && b instanceof List<?> lb) {
            LinkedHashSet<Object> set = new LinkedHashSet<>(la);
            set.addAll(lb);
            return new ArrayList<>(set);
        }
        if (a instanceof Number na && b instanceof Number nb) {
            return na.doubleValue() >= nb.doubleValue() ? a : b;
        }
        return b; // строки и прочее — побеждает новое
    }

    private void write(Map<String, Object> data) throws IOException {
        Files.createDirectories(backups);
        // 1. Дневной снимок: если сегодня его ещё нет — сохраняем состояние ДО изменения
        Path daily = backups.resolve("progress-" + LocalDate.now() + ".json");
        if (Files.exists(file) && !Files.exists(daily) && readMap(file) != null) {
            Files.copy(file, daily, StandardCopyOption.REPLACE_EXISTING);
            pruneBackups();
        }
        // 2. Предыдущая версия
        if (Files.exists(file) && readMap(file) != null) {
            Files.copy(file, prev, StandardCopyOption.REPLACE_EXISTING);
        }
        // 3. Атомарная запись
        Path tmp = dir.resolve("progress.json.tmp");
        Files.writeString(tmp, pretty(data), StandardCharsets.UTF_8);
        try {
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /** Красивый JSON — чтобы файл можно было прочитать глазами (и наставнику тоже). */
    private static String pretty(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder("{\n");
        int i = 0;
        for (Map.Entry<String, Object> e : data.entrySet()) {
            sb.append("  ").append(Json.quote(e.getKey())).append(": ").append(Json.stringify(e.getValue()));
            sb.append(++i < data.size() ? ",\n" : "\n");
        }
        return sb.append("}\n").toString();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> readMap(Path p) {
        try {
            if (!Files.isRegularFile(p)) return null;
            String text = Files.readString(p, StandardCharsets.UTF_8);
            if (text.isBlank()) return null;
            Object o = Json.parse(text);
            return o instanceof Map ? (Map<String, Object>) o : null;
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private void quarantine(Path p) {
        try {
            Path target = dir.resolve("progress.corrupt-" + LocalDateTime.now().format(STAMP) + ".json");
            Files.move(p, target, StandardCopyOption.REPLACE_EXISTING);
            System.err.println("Файл прогресса повреждён, отложен в " + target + ". Восстанавливаю из резервной копии.");
        } catch (IOException e) {
            System.err.println("Не удалось отложить повреждённый файл прогресса: " + e);
        }
    }

    /** Дневные снимки, отсортированные по дате (newestFirst — от новых к старым). */
    private List<Path> listBackups(boolean newestFirst) {
        if (!Files.isDirectory(backups)) return List.of();
        try (Stream<Path> s = Files.list(backups)) {
            Comparator<Path> byName = Comparator.comparing(x -> x.getFileName().toString());
            return s.filter(x -> x.getFileName().toString().matches("progress-\\d{4}-\\d{2}-\\d{2}\\.json"))
                    .sorted(newestFirst ? byName.reversed() : byName).toList();
        } catch (IOException e) {
            return List.of();
        }
    }

    private void pruneBackups() throws IOException {
        List<Path> all = listBackups(false);
        for (int i = 0; i < all.size() - KEEP_DAILY; i++) Files.deleteIfExists(all.get(i));
    }
}
