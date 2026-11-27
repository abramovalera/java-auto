import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

/**
 * Движок игры «Раннер». Запуск: java RunnerGame <файл-уровня> <seed>
 * Печатает в stdout JSON с кадрами забега. Код ученика — класс Javik с методом String decide(Radar radar).
 *
 * Правила хода:
 *  1. ученик смотрит на радар и возвращает команду: "RUN", "JUMP" или "DUCK";
 *  2. JUMP — Джавик в воздухе 4 хода (этот и три следующих), DUCK — пригнулся на 3 хода.
 *     Пока он в воздухе или пригнулся, новые команды не действуют;
 *  3. Джавик пробегает speed клеток (1 или 2), после каждой клетки — проверка столкновения:
 *     жук (LOW) на земле — опасен, если Джавик не в воздухе; летучая ошибка (HIGH) на уровне головы — опасна,
 *     если Джавик не пригнулся (в прыжке тоже врежется!).
 *  4. Приземлился прямо на жука или выпрямился под летучей ошибкой — тоже столкновение.
 *  Добежал до конца трассы — финиш.
 */
public class RunnerGame {

    static final int JUMP_TICKS = 4, DUCK_TICKS = 3, VIEW = 12;

    // ---- трасса ----
    private final int length;
    private final int maxTicks;
    private final List<int[]> obstacles = new ArrayList<>(); // {start, width, type(0 — жук LOW, 1 — летучая HIGH)}
    private final int speedUpAt; // с какой клетки скорость 2 (или length — никогда)

    // ---- состояние ----
    private int x;
    private int tick;
    private int airLeft;
    private int duckLeft;
    private String lastState = "run"; // как Джавик пробегал последний ход: run / jump / duck

    RunnerGame(Map<String, String> level, long seed) {
        this.length = Integer.parseInt(level.getOrDefault("length", "60"));
        this.maxTicks = Integer.parseInt(level.getOrDefault("ticks", String.valueOf(length * 2)));
        this.speedUpAt = Integer.parseInt(level.getOrDefault("speedUpAt", String.valueOf(length + 1)));
        generate(level, seed);
    }

    // =============================== API для Radar ===============================
    int speed() { return x >= speedUpAt ? 2 : 1; }

    private int[] next() {
        for (int[] o : obstacles) if (o[0] + o[1] - 1 > x) return o;
        return null;
    }

    int distanceToNext() {
        int[] o = next();
        if (o == null || o[0] - x > VIEW) return 99;
        return Math.max(0, o[0] - x);
    }

    boolean nextIsLow() {
        int[] o = next();
        return o != null && o[0] - x <= VIEW && o[2] == 0;
    }

    // =============================== генерация трассы ===============================
    private void generate(Map<String, String> level, long seed) {
        String types = level.getOrDefault("types", "low");
        int gapMin = Integer.parseInt(level.getOrDefault("gapMin", "4"));
        int gapMax = Integer.parseInt(level.getOrDefault("gapMax", "9"));
        int gapMinFast = Integer.parseInt(level.getOrDefault("gapMinFast", String.valueOf(gapMin + 3)));
        String[] lowW = level.getOrDefault("lowWidths", "1,2").split(",");
        // перебираем карты, пока «правильная» стратегия не пройдёт, а «наивные» — не провалятся
        for (long attempt = 0; attempt < 500; attempt++) {
            Random rnd = new Random(seed * 7919 + attempt);
            obstacles.clear();
            int pos = 8;
            while (true) {
                int type = types.equals("low") ? 0 : (rnd.nextInt(100) < 45 ? 1 : 0);
                int w = type == 1 ? 1 : Integer.parseInt(lowW[rnd.nextInt(lowW.length)].strip());
                if (pos + w >= length - 2) break;
                obstacles.add(new int[] {pos, w, type});
                int gMin = pos >= speedUpAt - 2 ? gapMinFast : gapMin;
                pos += w + gMin + rnd.nextInt(Math.max(1, gapMax - gapMin + 1));
            }
            if (acceptable(types)) return;
        }
    }

    private boolean acceptable(String types) {
        Function<Radar, String> good = r -> r.distance() <= 2 ? (r.isLow() ? "JUMP" : "DUCK") : "RUN";
        Function<Radar, String> early = r -> r.distance() <= 3 ? (r.isLow() ? "JUMP" : "DUCK") : "RUN";
        Function<Radar, String> exact1 = r -> r.distance() == 1 ? (r.isLow() ? "JUMP" : "DUCK") : "RUN";
        Function<Radar, String> onlyJump = r -> r.distance() <= 2 ? "JUMP" : "RUN";
        if (!"finish".equals(simulate(good))) return false;
        if ("finish".equals(simulate(early))) return false;               // прыжок за 3 клетки — рано (длинный жук)
        if (!types.equals("low") && "finish".equals(simulate(onlyJump))) return false; // нужны и прыжок, и присед
        if (speedUpAt <= length && "finish".equals(simulate(exact1))) return false;    // на скорости 2 «== 1» проскакивает
        return true;
    }

    private String simulate(Function<Radar, String> bot) {
        x = 0; tick = 0; airLeft = 0; duckLeft = 0;
        Radar radar = new Radar(this);
        String r = null;
        while (r == null) r = step(bot.apply(radar), null);
        x = 0; tick = 0; airLeft = 0; duckLeft = 0;
        return r;
    }

    private boolean obstacleAt(int cell, int type) {
        for (int[] o : obstacles) if (o[2] == type && cell >= o[0] && cell <= o[0] + o[1] - 1) return true;
        return false;
    }

    // =============================== один ход ===============================
    /** Выполнить команду. Возвращает null (бежим дальше), "finish", "crash" или "timeout". */
    private String step(String action, List<String> notes) {
        boolean busy = airLeft > 0 || duckLeft > 0;
        switch (action) {
            case "JUMP" -> { if (!busy) airLeft = JUMP_TICKS; else if (notes != null) notes.add("Джавик ещё " + (airLeft > 0 ? "в воздухе" : "пригнулся") + " — команда JUMP не сработала."); }
            case "DUCK" -> { if (!busy) duckLeft = DUCK_TICKS; else if (notes != null) notes.add("Джавик ещё " + (airLeft > 0 ? "в воздухе" : "пригнулся") + " — команда DUCK не сработала."); }
            case "RUN" -> { }
            default -> { if (notes != null) notes.add("⚠ Неизвестная команда «" + action + "». Джавик просто бежит. Возможные: RUN, JUMP, DUCK."); }
        }
        lastState = airLeft > 0 ? "jump" : duckLeft > 0 ? "duck" : "run";
        int s = speed();
        for (int k = 0; k < s; k++) {
            x++;
            for (int[] o : obstacles) {
                if (x >= o[0] && x <= o[0] + o[1] - 1) {
                    if (o[2] == 0 && airLeft == 0) return "crash";
                    if (o[2] == 1 && duckLeft == 0) return "crash";
                }
            }
            if (x >= length) break;
        }
        tick++;
        if (airLeft > 0 && --airLeft == 0 && obstacleAt(x, 0)) return "crash";   // приземлился на жука
        if (duckLeft > 0 && --duckLeft == 0 && obstacleAt(x, 1)) return "crash"; // выпрямился под летучей ошибкой
        if (x >= length) return "finish";
        if (tick >= maxTicks) return "timeout";
        return null;
    }

    // =============================== запуск ученика ===============================
    public static void main(String[] args) throws Exception {
        PrintStream realOut = new PrintStream(new java.io.FileOutputStream(java.io.FileDescriptor.out), true, StandardCharsets.UTF_8);
        Map<String, String> level = readLevel(Path.of(args[0]));
        long seed = Long.parseLong(args[1]);
        RunnerGame g = new RunnerGame(level, seed);
        realOut.println(g.play());
    }

    private String play() {
        Radar radar = new Radar(this);
        StringBuilder frames = new StringBuilder();
        PrintStream realOut = System.out;
        Object javik;
        try {
            javik = Class.forName("Javik").getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            return json("error", "Не удалось создать Джавика: класс Javik не найден или у него нет конструктора без параметров (" + e + ")", frames);
        }
        java.lang.reflect.Method decide;
        try {
            decide = javik.getClass().getMethod("decide", Radar.class);
        } catch (NoSuchMethodException e) {
            return json("error", "В классе Javik нет метода public String decide(Radar radar).", frames);
        }
        String result = null;
        while (result == null) {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            List<String> notes = new ArrayList<>();
            int fx = x, d = distanceToNext(), sp = speed();
            boolean low = nextIsLow();
            String action;
            try {
                System.setOut(new PrintStream(buf, true, StandardCharsets.UTF_8));
                Object a = decide.invoke(javik, radar);
                action = a == null ? "null" : a.toString().trim().toUpperCase();
            } catch (java.lang.reflect.InvocationTargetException e) {
                System.setOut(realOut);
                appendFrame(frames, fx, x, "💥", d, low, sp, "run", buf, List.of("Исключение в decide: " + e.getCause()));
                return json("error", "Твой метод decide упал с исключением: " + e.getCause(), frames);
            } catch (Throwable e) {
                System.setOut(realOut);
                return json("error", "Ошибка при вызове decide: " + e, frames);
            } finally {
                System.setOut(realOut);
            }
            result = step(action, notes);
            appendFrame(frames, fx, x, action, d, low, sp, lastState, buf, notes);
        }
        String message = switch (result) {
            case "finish" -> "Джавик добежал до финиша за " + tick + " ходов! 🏁";
            case "crash" -> "Столкновение на ходу " + (tick + 1) + " (клетка " + x + "). Посмотри, что показывал радар и какую команду вернул decide.";
            default -> "Время вышло: за " + maxTicks + " ходов Джавик не добежал.";
        };
        return json(result, message, frames);
    }

    private void appendFrame(StringBuilder frames, int from, int to, String action, int d, boolean low, int sp, String state, ByteArrayOutputStream buf, List<String> notes) {
        if (frames.length() > 0) frames.append(',');
        List<String> log = new ArrayList<>();
        String printed = buf.toString(StandardCharsets.UTF_8).strip();
        if (!printed.isEmpty()) for (String l : printed.split("\\R")) if (log.size() < 5) log.add(l.length() > 120 ? l.substring(0, 120) + "…" : l);
        log.addAll(notes);
        frames.append("{\"from\":").append(from).append(",\"to\":").append(to).append(",\"action\":").append(q(action))
                .append(",\"d\":").append(d).append(",\"low\":").append(low).append(",\"speed\":").append(sp)
                .append(",\"state\":").append(q(state)).append(",\"log\":[");
        for (int i = 0; i < log.size(); i++) frames.append(i > 0 ? "," : "").append(q(log.get(i)));
        frames.append("]}");
    }

    private String json(String result, String message, StringBuilder frames) {
        StringBuilder o = new StringBuilder("[");
        for (int[] ob : obstacles) {
            if (o.length() > 1) o.append(',');
            o.append('[').append(ob[0]).append(',').append(ob[1]).append(',').append(ob[2]).append(']');
        }
        o.append(']');
        return "{\"result\":" + q(result) + ",\"message\":" + q(message) + ",\"ticks\":" + tick + ",\"length\":" + length
                + ",\"speedUpAt\":" + speedUpAt + ",\"obstacles\":" + o + ",\"frames\":[" + frames + "]}";
    }

    private static String q(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> { if (c < 0x20) sb.append(String.format("\\u%04x", (int) c)); else sb.append(c); }
            }
        }
        return sb.append('"').toString();
    }

    private static Map<String, String> readLevel(Path p) throws Exception {
        Map<String, String> m = new LinkedHashMap<>();
        for (String line : Files.readAllLines(p, StandardCharsets.UTF_8)) {
            line = line.strip();
            if (line.isEmpty() || line.startsWith("#")) continue;
            int eq = line.indexOf('=');
            if (eq > 0) m.put(line.substring(0, eq).strip(), line.substring(eq + 1).strip());
        }
        return m;
    }
}
