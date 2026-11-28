import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Движок игры «Джавик-курьер». Запуск: java CourierGame <файл-уровня> <seed>
 * Печатает в stdout JSON: карта, кадры поездки, итог. Код ученика — класс из параметра class уровня:
 *  - entry=decide: static String decide(CourierRobot robot) — вызывается на каждом ходу, возвращает команду;
 *  - entry=run:    static void run(CourierRobot robot) — вызывается один раз, команды — методами робота.
 *
 * Параметры уровня (ключ=значение):
 *  class, entry, map (corridor|forks|street|stairs|parcels|shuttle|houses|middle|town),
 *  goal (reach|deliver), home (true — после доставки вернуться на склад), night (true — после DROP датчики стен выключены),
 *  capacity (сколько посылок везёт за раз), output (route|compressed|delivered|report), noTarget (true — atTarget не работает),
 *  orders (fifo|repeats), goToLimit (unique), ticks (лимит ходов), minLen, maxLen.
 */
public class CourierGame {

    /** Конец поездки: врезался, ошибка, лимит ходов. Error, чтобы случайный catch (Exception e) ученика его не проглотил. */
    static final class Stop extends Error {
        final String result;
        Stop(String result, String message) { super(message, null, false, false); this.result = result; }
    }

    static final int[] DX = {0, 1, 0, -1}, DY = {-1, 0, 1, 0}; // 0 — север, 1 — восток, 2 — юг, 3 — запад
    static final String[] DIR_NAME = {"север", "восток", "юг", "запад"};
    static final String[] PLACES = {"Аптека", "Библиотека", "Вокзал", "Кафе", "Пекарня", "Почта", "Школа"};

    final Map<String, String> lv;
    final Random rnd;
    int w = 17, h = 11;
    boolean[][] road;
    int[][] parcels;
    boolean[][] target;
    String[][] sign;
    String[][] house;
    int[][] accepts;          // сколько посылок примет адрес (-1 — сколько угодно)
    int depotX, depotY, startDir;
    int x, y, dir, carry, capacity, ticks, maxTicks, totalParcels, delivered;
    boolean night, noTarget, turnedHere, dropped;
    int[] housesArr = new int[0];
    final List<String> log = new ArrayList<>();          // команды движения до первой доставки — для output=route
    final List<String> deliveredTo = new ArrayList<>();
    final List<String> initialOrders = new ArrayList<>(), arrival = new ArrayList<>();
    final Map<Integer, List<String>> arriveAfter = new HashMap<>();
    final List<String> pendingNew = new ArrayList<>();
    int goToCalls, goToLimit = Integer.MAX_VALUE;
    final StringBuilder frames = new StringBuilder();
    int frameCount;
    final List<String> ask = new ArrayList<>();
    final List<int[]> changed = new ArrayList<>();

    CourierGame(Map<String, String> lv, long seed) {
        this.lv = lv;
        this.rnd = new Random(seed * 7919 + lv.getOrDefault("map", "").hashCode());
        capacity = Integer.parseInt(lv.getOrDefault("capacity", "1000"));
        night = bool("night");
        noTarget = bool("noTarget");
        build();
        x = depotX; y = depotY; dir = startDir;
        maxTicks = Integer.parseInt(lv.getOrDefault("ticks", "400"));
        if (lv.containsKey("ticksFactor")) maxTicks = (int) (pathCost() * Double.parseDouble(lv.get("ticksFactor"))) + 4;
    }

    boolean bool(String k) { return "true".equals(lv.get(k)); }
    int num(String k, int def) { return Integer.parseInt(lv.getOrDefault(k, String.valueOf(def))); }

    // ============================ построение карт ============================
    void alloc() {
        road = new boolean[h][w]; parcels = new int[h][w]; target = new boolean[h][w];
        sign = new String[h][w]; house = new String[h][w]; accepts = new int[h][w];
        for (int[] row : accepts) Arrays.fill(row, -1);
    }

    void build() {
        String map = lv.getOrDefault("map", "corridor");
        for (int attempt = 0; attempt < 500; attempt++) {
            alloc();
            boolean ok = switch (map) {
                case "corridor" -> corridor(false);
                case "forks" -> corridor(true);
                case "street" -> street(num("minLen", 5), num("maxLen", 15));
                case "stairs" -> stairs();
                case "parcels" -> parcelStreet();
                case "shuttle" -> shuttle();
                case "houses" -> housesStreet();
                case "middle" -> middle();
                case "town" -> town();
                default -> throw new IllegalArgumentException("нет карты " + map);
            };
            if (ok) {
                for (int[] row : parcels) for (int p : row) totalParcels += p;
                totalParcels += carry;
                return;
            }
        }
        throw new IllegalStateException("не удалось построить карту " + map);
    }

    List<int[]> mainPath = new ArrayList<>();

    /** Извилистый коридор без развилок; с forks=true — с тупиковыми ответвлениями и табличками на развилках. */
    boolean corridor(boolean forks) {
        int minLen = num("minLen", 14), maxLen = num("maxLen", 26);
        int len = minLen + rnd.nextInt(maxLen - minLen + 1);
        int cx = 1 + rnd.nextInt(w - 2), cy = 1 + rnd.nextInt(h - 2);
        int d = rnd.nextInt(4);
        mainPath.clear();
        mainPath.add(new int[]{cx, cy});
        road[cy][cx] = true;
        int straight = 0;
        while (mainPath.size() < len) {
            List<Integer> opts = new ArrayList<>();
            for (int nd = 0; nd < 4; nd++) {
                if (nd == (d + 2) % 4) continue;
                if (canDig(cx + DX[nd], cy + DY[nd], cx, cy)) opts.add(nd);
            }
            if (opts.isEmpty()) return false;
            int nd;
            if (opts.contains(d) && (straight < 2 || rnd.nextInt(3) > 0) && straight < 5) nd = d;
            else nd = opts.get(rnd.nextInt(opts.size()));
            straight = nd == d ? straight + 1 : 0;
            d = nd; cx += DX[d]; cy += DY[d];
            road[cy][cx] = true;
            mainPath.add(new int[]{cx, cy});
        }
        int turns = 0;
        for (int i = 2; i < mainPath.size(); i++) if (dirBetween(mainPath.get(i - 2), mainPath.get(i - 1)) != dirBetween(mainPath.get(i - 1), mainPath.get(i))) turns++;
        if (turns < num("minTurns", 3)) return false;
        int[] s = mainPath.get(0), e = mainPath.get(mainPath.size() - 1);
        depotX = s[0]; depotY = s[1]; startDir = dirBetween(s, mainPath.get(1));
        target[e[1]][e[0]] = true;
        if (bool("parcel")) {
            int[] p = mainPath.get(3 + rnd.nextInt(mainPath.size() - 6));
            parcels[p[1]][p[0]] = 1;
        }
        if (bool("carryStart")) carry = 1;
        if (forks) {
            int made = 0, traps = 0;
            for (int tries = 0; tries < 60 && made < num("branches", 3); tries++) {
                int i = 2 + rnd.nextInt(mainPath.size() - 4);
                int[] j = mainPath.get(i);
                int bd = rnd.nextInt(4);
                int bl = 2 + rnd.nextInt(3);
                List<int[]> cells = new ArrayList<>();
                int bx = j[0], by = j[1];
                boolean good = true;
                for (int k = 0; k < bl; k++) {
                    int nx = bx + DX[bd], ny = by + DY[bd];
                    if (!canDig(nx, ny, bx, by)) { good = false; break; }
                    cells.add(new int[]{nx, ny}); road[ny][nx] = true; bx = nx; by = ny;
                }
                if (!good || cells.size() < 2) { for (int[] c : cells) road[c[1]][c[0]] = false; continue; }
                made++;
            }
            if (made < num("branches", 3)) return false;
            // таблички на всех развилках главного пути: куда ехать дальше относительно того, как Джавик въехал
            for (int i = 1; i < mainPath.size() - 1; i++) {
                int[] c = mainPath.get(i);
                int exits = 0;
                for (int k = 0; k < 4; k++) if (isRoad(c[0] + DX[k], c[1] + DY[k])) exits++;
                if (exits >= 3) {
                    int in = dirBetween(mainPath.get(i - 1), c), out = dirBetween(c, mainPath.get(i + 1));
                    sign[c[1]][c[0]] = out == in ? "AHEAD" : out == (in + 3) % 4 ? "LEFT" : "RIGHT";
                    // «ловушка»: без таблички правило «вперёд, иначе налево, иначе направо» увело бы в тупик
                    int naive = isRoad(c[0] + DX[in], c[1] + DY[in]) ? in : isRoad(c[0] + DX[(in + 3) % 4], c[1] + DY[(in + 3) % 4]) ? (in + 3) % 4 : (in + 1) % 4;
                    if (naive != out) traps++;
                }
            }
            if (traps < num("traps", 2)) return false;
        }
        return true;
    }

    /** Можно ли сделать клетку дорогой, не задев других дорог (кроме той, откуда пришли). */
    boolean canDig(int nx, int ny, int fx, int fy) {
        if (nx < 1 || ny < 1 || nx > w - 2 || ny > h - 2 || road[ny][nx]) return false;
        for (int k = 0; k < 4; k++) {
            int ax = nx + DX[k], ay = ny + DY[k];
            if ((ax != fx || ay != fy) && isRoad(ax, ay)) return false;
        }
        return true;
    }

    boolean isRoad(int cx, int cy) { return cx >= 0 && cy >= 0 && cx < w && cy < h && road[cy][cx]; }

    static int dirBetween(int[] a, int[] b) {
        for (int k = 0; k < 4; k++) if (a[0] + DX[k] == b[0] && a[1] + DY[k] == b[1]) return k;
        throw new IllegalStateException();
    }

    boolean street(int minLen, int maxLen) {
        int len = minLen + rnd.nextInt(maxLen - minLen + 1);
        len = Math.min(len, w - 2);
        int row = 1 + rnd.nextInt(h - 2), x0 = 1 + rnd.nextInt(w - 1 - len);
        for (int i = 0; i < len; i++) road[row][x0 + i] = true;
        depotX = x0; depotY = row; startDir = 1;
        target[row][x0 + len - 1] = true;
        mainPath.clear();
        for (int i = 0; i < len; i++) mainPath.add(new int[]{x0 + i, row});
        return true;
    }

    /** Лестница: вперёд на восток, поворот на север или юг, снова на восток… */
    boolean stairs() {
        int cx = 1, cy = rnd.nextBoolean() ? h - 2 : 1, vert = cy == 1 ? 2 : 0;
        road[cy][cx] = true;
        depotX = cx; depotY = cy; startDir = 1;
        mainPath.clear(); mainPath.add(new int[]{cx, cy});
        int steps = 0;
        while (true) {
            int a = 2 + rnd.nextInt(3);
            for (int i = 0; i < a; i++) { if (cx + 1 > w - 2) break; cx++; road[cy][cx] = true; mainPath.add(new int[]{cx, cy}); }
            if (cx >= w - 3) break;
            int b = 1 + rnd.nextInt(2);
            int ny = cy + DY[vert] * b;
            if (ny < 1 || ny > h - 2) break;
            for (int i = 0; i < b; i++) { cy += DY[vert]; road[cy][cx] = true; mainPath.add(new int[]{cx, cy}); }
            steps++;
        }
        if (steps < 2) return false;
        int[] e = mainPath.get(mainPath.size() - 1);
        target[e[1]][e[0]] = true;
        return true;
    }

    boolean parcelStreet() {
        street(num("minLen", 10), num("maxLen", 15));
        int n = 3 + rnd.nextInt(5);
        for (int i = 0; i < n; i++) {
            int[] c = mainPath.get(1 + rnd.nextInt(mainPath.size() - 2));
            parcels[c[1]][c[0]] = Math.min(2, parcels[c[1]][c[0]] + 1);
        }
        return true;
    }

    boolean shuttle() {
        street(num("minLen", 5), num("maxLen", 12));
        parcels[depotY][depotX] = 3;
        int[] e = mainPath.get(mainPath.size() - 1);
        target[e[1]][e[0]] = true;
        return true;
    }

    /** Улица на восток, дома — ниши сверху (слева по ходу). houses() — номера клеток от склада, по возрастанию. */
    boolean housesStreet() {
        int len = 10 + rnd.nextInt(5);
        int row = 2 + rnd.nextInt(h - 3), x0 = 1;
        for (int i = 0; i < len; i++) road[row][x0 + i] = true;
        depotX = x0; depotY = row; startDir = 1;
        int n = 3 + rnd.nextInt(2);
        TreeSet<Integer> pos = new TreeSet<>();
        while (pos.size() < n) {
            int p = 1 + rnd.nextInt(len - 1);
            if (!pos.contains(p - 1) && !pos.contains(p + 1)) pos.add(p);
        }
        housesArr = pos.stream().mapToInt(Integer::intValue).toArray();
        for (int p : housesArr) { road[row - 1][x0 + p] = true; target[row - 1][x0 + p] = true; accepts[row - 1][x0 + p] = 1; }
        carry = n;
        return true;
    }

    boolean middle() {
        street(num("minLen", 6), num("maxLen", 14));
        int len = mainPath.size();
        target[depotY][depotX + len - 1] = false;
        int[] m = mainPath.get((len - 1) / 2);
        target[m[1]][m[0]] = true;
        carry = 1;
        return true;
    }

    /** Район: кольцевая дорога и поперечная улица, на дорогах — адреса. */
    boolean town() {
        int left = 1, right = w - 2, top = 1, bottom = h - 2;
        for (int cx = left; cx <= right; cx++) { road[top][cx] = true; road[bottom][cx] = true; }
        for (int cy = top; cy <= bottom; cy++) { road[cy][left] = true; road[cy][right] = true; }
        int mid = 5 + rnd.nextInt(w - 10);
        for (int cy = top; cy <= bottom; cy++) road[cy][mid] = true;
        depotX = left; depotY = bottom; startDir = 0;
        List<int[]> cells = new ArrayList<>();
        for (int cy = 0; cy < h; cy++) for (int cx = 0; cx < w; cx++) {
            if (!road[cy][cx] || (cx == depotX && cy == depotY)) continue;
            int exits = 0;
            for (int k = 0; k < 4; k++) if (isRoad(cx + DX[k], cy + DY[k])) exits++;
            if (exits == 2) cells.add(new int[]{cx, cy});
        }
        Collections.shuffle(cells, rnd);
        List<String> names = new ArrayList<>(Arrays.asList(PLACES));
        Collections.shuffle(names, rnd);
        int n = 5;
        for (int i = 0; i < n; i++) { int[] c = cells.get(i * 3); house[c[1]][c[0]] = names.get(i); target[c[1]][c[0]] = true; }
        List<String> used = names.subList(0, n);
        String kind = lv.getOrDefault("orders", "fifo");
        if (kind.equals("fifo")) {
            List<String> all = new ArrayList<>();
            for (int i = 0; i < 6; i++) all.add(used.get(rnd.nextInt(n)));
            initialOrders.addAll(all.subList(0, 3));
            arriveAfter.put(1, new ArrayList<>(all.subList(3, 4)));
            arriveAfter.put(3, new ArrayList<>(all.subList(4, 6)));
            arrival.addAll(all);
        } else {
            int total = 6 + rnd.nextInt(4);
            for (int i = 0; i < total; i++) initialOrders.add(used.get(rnd.nextInt(n)));
            if (new HashSet<>(initialOrders).size() == initialOrders.size()) return false;
            arrival.addAll(initialOrders);
        }
        carry = arrival.size();
        if ("unique".equals(lv.get("goToLimit"))) goToLimit = new HashSet<>(arrival).size();
        return true;
    }

    /** Сколько ходов занимает правильный путь — для лимита ходов на уровне с развилками. */
    int pathCost() {
        int cost = 0, d = startDir;
        for (int i = 1; i < mainPath.size(); i++) {
            int nd = dirBetween(mainPath.get(i - 1), mainPath.get(i));
            if (nd != d) cost += nd == (d + 2) % 4 ? 2 : 1;
            d = nd; cost++;
        }
        return cost + 2 + (bool("parcel") ? 2 : 0) + 1;
    }

    // ============================ датчики ============================
    boolean wall(String name, int turn) {
        if (night && dropped) throw new Stop("error", "Ночью датчики стен не работают: возвращайся на склад по записанному маршруту.");
        int d = (dir + turn + 4) % 4;
        boolean v = !isRoad(x + DX[d], y + DY[d]);
        note(name + " = " + v);
        return v;
    }

    boolean parcelHere() { boolean v = parcels[y][x] > 0; note("parcelHere() = " + v); return v; }
    boolean carrying() { boolean v = carry > 0; note("carrying() = " + v); return v; }
    boolean atDepot() { boolean v = x == depotX && y == depotY; note("atDepot() = " + v); return v; }

    boolean atTarget() {
        if (noTarget) throw new Stop("error", "На этой улице нет табличек с адресом: atTarget() не работает. Считай клетки.");
        boolean v = target[y][x];
        note("atTarget() = " + v);
        return v;
    }

    String sign() {
        String v = turnedHere || sign[y][x] == null ? "" : sign[y][x];
        note("sign() = \"" + v + "\"");
        return new String(v); // новый объект: строки сравнивают через equals, а == здесь честно не сработает
    }

    int[] houses() { note("houses() = " + Arrays.toString(housesArr)); return housesArr.clone(); }
    String houseName() { String v = house[y][x] == null ? "" : house[y][x]; note("houseName() = \"" + v + "\""); return new String(v); }

    List<String> orders() { note("orders() = " + initialOrders); return new ArrayList<>(initialOrders); }

    List<String> newOrders() {
        List<String> v = new ArrayList<>(pendingNew);
        pendingNew.clear();
        note("newOrders() = " + v);
        return v;
    }

    void note(String s) { if (ask.size() < 12) ask.add(s); }

    // ============================ команды ============================
    synchronized void act(String cmd) {
        if (++ticks > maxTicks) throw new Stop("timeout", "Ходы кончились (лимит " + maxTicks + "). Похоже, Джавик ездит по кругу или заехал не туда.");
        switch (cmd) {
            case "MOVE" -> {
                int nx = x + DX[dir], ny = y + DY[dir];
                if (!isRoad(nx, ny)) {
                    frame(cmd, "💥 Врезался в стену!");
                    if (night && dropped) throw new Stop("crash", "Джавик врезался в стену на обратном пути (ход " + ticks + "). Ночью датчики не работают — проверь сам маршрут обратно: разворот в начале, обход массива с конца и замену LEFT ↔ RIGHT.");
                    throw new Stop("crash", "Джавик врезался в стену на ходу " + ticks + ". Перед MOVE проверь wallAhead().");
                }
                x = nx; y = ny; turnedHere = false;
                if (!dropped) log.add(cmd);
            }
            case "LEFT", "RIGHT" -> {
                dir = (dir + (cmd.equals("LEFT") ? 3 : 1)) % 4;
                turnedHere = true;
                if (!dropped) log.add(cmd);
            }
            case "TAKE" -> {
                if (parcels[y][x] == 0) { frame(cmd, "Здесь нет посылки"); throw new Stop("error", "TAKE на ходу " + ticks + ": на этой клетке нет посылки."); }
                if (carry >= capacity) { frame(cmd, "Руки заняты"); throw new Stop("error", "TAKE на ходу " + ticks + ": Джавик уже везёт " + carry + " — больше не помещается."); }
                parcels[y][x]--; carry++;
                changed.add(new int[]{x, y, parcels[y][x]});
            }
            case "DROP" -> {
                if (carry == 0) { frame(cmd, "Нечего отдавать"); throw new Stop("error", "DROP на ходу " + ticks + ": у Джавика нет посылки."); }
                if (!target[y][x]) { frame(cmd, "Здесь не адрес доставки"); throw new Stop("error", "DROP на ходу " + ticks + ": это не адрес доставки."); }
                if (accepts[y][x] == 0) { frame(cmd, "Сюда уже доставили"); throw new Stop("error", "DROP на ходу " + ticks + ": этот дом свою посылку уже получил."); }
                if (house[y][x] != null) {
                    long wanted = arrival.stream().filter(house[y][x]::equals).count() - deliveredTo.stream().filter(house[y][x]::equals).count();
                    if (wanted <= 0) { frame(cmd, "Адрес не заказывал"); throw new Stop("error", "DROP на ходу " + ticks + ": «" + house[y][x] + "» больше ничего не заказывал."); }
                    deliveredTo.add(house[y][x]);
                }
                if (accepts[y][x] > 0) accepts[y][x]--;
                carry--; delivered++; dropped = true;
                changed.add(new int[]{x, y, -1});
                List<String> arrived = arriveAfter.get(delivered);
                if (arrived != null) pendingNew.addAll(arrived);
            }
            default -> {
                frame(String.valueOf(cmd), "Нет такой команды");
                throw new Stop("error", "Команда " + (cmd == null ? "null" : "\"" + cmd + "\"") + " на ходу " + (ticks) + " — такой нет. Можно: MOVE, LEFT, RIGHT, TAKE, DROP.");
            }
        }
        frame(cmd, null);
    }

    void goTo(String address) {
        note("goTo(\"" + address + "\")");
        if (++goToCalls > goToLimit) throw new Stop("error", "goTo вызван " + goToCalls + "-й раз, а на этом уровне можно не больше " + goToLimit + ": к каждому дому — один заезд.");
        int tx = -1, ty = -1;
        for (int cy = 0; cy < h; cy++) for (int cx = 0; cx < w; cx++) if (address != null && address.equals(house[cy][cx])) { tx = cx; ty = cy; }
        if (tx < 0) throw new Stop("error", "goTo(\"" + address + "\"): такого адреса в районе нет.");
        // поиск пути в ширину по дорогам
        int[][] prev = new int[h * w][];
        ArrayDeque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{x, y});
        prev[y * w + x] = new int[]{x, y};
        while (!q.isEmpty()) {
            int[] c = q.poll();
            for (int k = 0; k < 4; k++) {
                int nx = c[0] + DX[k], ny = c[1] + DY[k];
                if (isRoad(nx, ny) && prev[ny * w + nx] == null) { prev[ny * w + nx] = c; q.add(new int[]{nx, ny}); }
            }
        }
        LinkedList<int[]> path = new LinkedList<>();
        for (int[] c = {tx, ty}; !(c[0] == x && c[1] == y); c = prev[c[1] * w + c[0]]) path.addFirst(c);
        autopilot = address;
        try {
            for (int[] c : path) {
                int nd = dirBetween(new int[]{x, y}, c);
                while (dir != nd) act((nd - dir + 4) % 4 == 3 ? "LEFT" : "RIGHT");
                act("MOVE");
            }
        } finally {
            autopilot = null;
        }
    }

    // ============================ кадры и итог ============================
    void frame(String cmd, String noteText) {
        if (frameCount++ > 1500) return;
        if (frames.length() > 0) frames.append(',');
        frames.append("{\"x\":").append(x).append(",\"y\":").append(y).append(",\"d\":").append(dir)
              .append(",\"c\":").append(carry).append(",\"cmd\":").append(q(autopilot == null ? cmd : cmd + " · автопилот goTo(\"" + autopilot + "\")"));
        if (night && dropped) frames.append(",\"night\":true");
        frames.append(",\"ask\":[");
        for (int i = 0; i < ask.size(); i++) frames.append(i > 0 ? "," : "").append(q(ask.get(i)));
        frames.append("]");
        if (!changed.isEmpty()) {
            frames.append(",\"p\":[");
            for (int i = 0; i < changed.size(); i++) { int[] c = changed.get(i); frames.append(i > 0 ? "," : "").append('[').append(c[0]).append(',').append(c[1]).append(',').append(c[2]).append(']'); }
            frames.append(']');
        }
        if (noteText != null) frames.append(",\"note\":").append(q(noteText));
        frames.append('}');
        ask.clear();
        changed.clear();
    }

    /** Проверка цели после поездки. null — всё выполнено, иначе — что не так. */
    String missing(String output) {
        String goal = lv.getOrDefault("goal", "reach");
        if (goal.equals("reach") && !target[y][x]) return "Джавик остановился, не доехав до 🏠.";
        if (goal.equals("deliver")) {
            if (!pendingNew.isEmpty() || deliveredTo.size() < arrival.size() && !arrival.isEmpty())
                return "Доставлено не всё: остались заказы. Не забывай про newOrders() — новые заказы приходят по ходу смены.";
            if (delivered < totalParcels) return "Доставлено посылок: " + delivered + " из " + totalParcels + ".";
            if (lv.getOrDefault("orders", "").equals("fifo") && !deliveredTo.equals(arrival))
                return "Порядок доставки нарушен. Нужно по очереди поступления: " + arrival + ", а было: " + deliveredTo + ".";
        }
        if (bool("home") && !(x == depotX && y == depotY)) return "Посылка доставлена, но Джавик не вернулся на склад 🏁.";
        String exp = expectedOutput();
        String got = normalize(output);
        if ("route".equals(lv.get("output"))) got = got.split("\n", 2)[0]; // на уровнях с полным маршрутом важна только первая строка
        if (exp != null && !got.equals(normalize(exp))) outputMismatch = true;
        if (outputMismatch) return "Программа напечатала не то. Ожидалось:\n" + exp + "\nА было:\n" + (output.isBlank() ? "(ничего)" : output.strip());
        return null;
    }

    String expectedOutput() {
        String kind = lv.get("output");
        if (kind == null) return null;
        switch (kind) {
            case "route": return "Маршрут: " + String.join(" ", log);
            case "compressed": {
                List<String> parts = new ArrayList<>();
                for (int i = 0; i < log.size(); ) {
                    int j = i;
                    while (j < log.size() && log.get(j).equals(log.get(i))) j++;
                    parts.add(j - i == 1 ? log.get(i) : log.get(i) + " x" + (j - i));
                    i = j;
                }
                return "Маршрут: " + String.join(" ", log) + "\nКратко: " + String.join(", ", parts);
            }
            case "delivered": return "Доставлено: " + totalParcels;
            case "report": {
                TreeMap<String, Integer> m = new TreeMap<>();
                for (String a : deliveredTo) m.merge(a, 1, Integer::sum);
                StringBuilder sb = new StringBuilder();
                m.forEach((k, v) -> sb.append(k).append(": ").append(v).append('\n'));
                return sb.append("Всего: ").append(deliveredTo.size()).toString();
            }
            default: return null;
        }
    }

    static String normalize(String s) {
        StringBuilder sb = new StringBuilder();
        for (String line : s.strip().split("\\R")) sb.append(line.stripTrailing()).append('\n');
        return sb.toString().strip();
    }

    String json(String result, String message, String output) {
        StringBuilder g = new StringBuilder("[");
        for (int cy = 0; cy < h; cy++) {
            StringBuilder row = new StringBuilder();
            for (int cx = 0; cx < w; cx++) row.append(road[cy][cx] ? '.' : '#');
            g.append(cy > 0 ? "," : "").append(q(row.toString()));
        }
        g.append(']');
        StringBuilder items = new StringBuilder("[");
        boolean first = true;
        for (int cy = 0; cy < h; cy++) for (int cx = 0; cx < w; cx++) {
            if (!target[cy][cx] && parcels0[cy][cx] == 0 && sign[cy][cx] == null && house[cy][cx] == null) continue;
            items.append(first ? "" : ",").append("{\"x\":").append(cx).append(",\"y\":").append(cy);
            if (target[cy][cx]) items.append(",\"t\":true");
            if (parcels0[cy][cx] > 0) items.append(",\"p\":").append(parcels0[cy][cx]);
            if (sign[cy][cx] != null) items.append(",\"s\":").append(q(sign[cy][cx]));
            if (house[cy][cx] != null) items.append(",\"n\":").append(q(house[cy][cx]));
            items.append('}');
            first = false;
        }
        items.append(']');
        String exp = expectedOutput();
        return "{\"result\":" + q(result) + ",\"message\":" + q(message) + ",\"w\":" + w + ",\"h\":" + h + ",\"grid\":" + g
                + ",\"items\":" + items + ",\"start\":[" + depotX + "," + depotY + "," + startDir + "],\"carry0\":" + carry0
                + ",\"maxTicks\":" + maxTicks + ",\"output\":" + q(output) + (outputMismatch ? ",\"expected\":" + q(exp) : "")
                + ",\"frames\":[" + frames + "]}";
    }

    int[][] parcels0;
    int carry0;
    boolean outputMismatch;
    String autopilot;

    static String q(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> { }
                case '\t' -> sb.append("\\t");
                default -> { if (c < 32) sb.append(String.format("\\u%04x", (int) c)); else sb.append(c); }
            }
        }
        return sb.append('"').toString();
    }

    static Map<String, String> readLevel(Path file) throws Exception {
        Map<String, String> m = new HashMap<>();
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            line = line.strip();
            if (line.isEmpty() || line.startsWith("#")) continue;
            int eq = line.indexOf('=');
            if (eq > 0) m.put(line.substring(0, eq).strip(), line.substring(eq + 1).strip());
        }
        return m;
    }

    // ============================ запуск ============================
    public static void main(String[] args) throws Exception {
        Map<String, String> lv = readLevel(Path.of(args[0]));
        CourierGame g = new CourierGame(lv, Long.parseLong(args[1]));
        g.parcels0 = new int[g.h][];
        for (int i = 0; i < g.h; i++) g.parcels0[i] = g.parcels[i].clone();
        g.carry0 = g.carry;
        PrintStream realOut = System.out;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        String result = "finish", message;
        String cls = lv.getOrDefault("class", "CourierPart1"), entry = lv.getOrDefault("entry", "decide");
        Method m;
        Object self = null;
        try {
            Class<?> c = Class.forName(cls);
            m = c.getDeclaredMethod(entry, CourierRobot.class);
            m.setAccessible(true);
            if (!Modifier.isStatic(m.getModifiers())) {
                var ctor = c.getDeclaredConstructor();
                ctor.setAccessible(true);
                self = ctor.newInstance();
            }
        } catch (ClassNotFoundException e) {
            realOut.println(g.json("error", "Класс " + cls + " не найден.", ""));
            return;
        } catch (NoSuchMethodException e) {
            String sig = entry.equals("decide") ? "public static String decide(CourierRobot robot)" : "public static void run(CourierRobot robot)";
            realOut.println(g.json("error", "В классе " + cls + " нет метода " + sig + ".", ""));
            return;
        }
        CourierRobot robot = new CourierRobot(g);
        System.setOut(new PrintStream(buf, true, StandardCharsets.UTF_8));
        final Method method = m;
        final Object target = self;
        final Throwable[] failure = new Throwable[1];
        Thread player = new Thread(() -> {
            try {
                if (entry.equals("decide")) {
                    while (true) {
                        if (g.goalReachedForDecide()) break;
                        Object cmd = method.invoke(target, robot);
                        g.act(cmd == null ? null : cmd.toString().strip());
                    }
                } else {
                    method.invoke(target, robot);
                }
            } catch (Throwable t) {
                failure[0] = t;
            }
        }, "courier");
        player.setDaemon(true);
        player.start();
        // ждём, пока Джавик ездит; если 3 секунды подряд нет ни одного хода — код завис в цикле без команд
        int lastTicks = -1;
        long quietSince = System.currentTimeMillis();
        while (player.isAlive()) {
            player.join(200);
            synchronized (g) { if (g.ticks != lastTicks) { lastTicks = g.ticks; quietSince = System.currentTimeMillis(); } }
            if (player.isAlive() && System.currentTimeMillis() - quietSince > 3000) break;
        }
        try {
            if (player.isAlive()) {
                synchronized (g) { g.frame("⏳", "Завис: команд роботу больше нет"); }
                throw new Stop("hang", "Программа зависла после хода " + g.ticks + ": цикл крутится, а команд роботу нет (ни move, ни поворотов). Посмотри на последний ход — какое условие цикла так и не стало ложным и какой ветки с командой не хватает?");
            }
            if (failure[0] != null) throw failure[0];
            message = null;
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof Stop s) { result = s.result; message = s.getMessage(); }
            else {
                result = "error";
                StackTraceElement where = Arrays.stream(t.getStackTrace()).filter(st -> st.getClassName().startsWith(cls)).findFirst().orElse(null);
                message = "Твой код упал с исключением " + t.getClass().getSimpleName() + (t.getMessage() != null ? ": " + t.getMessage() : "")
                        + (where != null ? " (строка " + where.getLineNumber() + ")" : "") + ".";
                g.frame("💥", "Исключение: " + t.getClass().getSimpleName());
            }
        } catch (Stop s) {
            result = s.result; message = s.getMessage();
        } catch (Throwable t) {
            result = "error"; message = "Ошибка при запуске: " + t;
        } finally {
            System.setOut(realOut);
        }
        String output = buf.toString(StandardCharsets.UTF_8);
        if ("finish".equals(result)) {
            String miss = g.missing(output);
            if (miss != null) { result = "fail"; message = miss; }
            else message = "Готово! Ходов: " + g.ticks + ".";
        }
        synchronized (g) { realOut.println(g.json(result, message, output)); }
        realOut.flush();
        System.exit(0); // поток с зависшим кодом ученика не даст JVM закончиться сама
    }

    /** Для режима decide: поездка закончена, когда цель выполнена (без возврата домой — в части 1 его нет). */
    boolean goalReachedForDecide() {
        String goal = lv.getOrDefault("goal", "reach");
        if (goal.equals("reach")) return target[y][x];
        return delivered >= totalParcels;
    }
}
