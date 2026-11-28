import java.util.List;

/**
 * Робот-курьер Джавик (настоящая версия, работает на сайте курса вместе с движком CourierGame).
 * Копия с тем же набором методов лежит у ученика в workspace/courier/CourierRobot.java.
 */
public class CourierRobot {

    private final CourierGame game;

    CourierRobot(CourierGame game) {
        this.game = game;
    }

    // ---------- датчики ----------
    public boolean wallAhead() { return game.wall("wallAhead()", 0); }
    public boolean wallLeft() { return game.wall("wallLeft()", -1); }
    public boolean wallRight() { return game.wall("wallRight()", 1); }
    public boolean parcelHere() { return game.parcelHere(); }
    public boolean carrying() { return game.carrying(); }
    public boolean atTarget() { return game.atTarget(); }
    public boolean atDepot() { return game.atDepot(); }
    public String sign() { return game.sign(); }

    // ---------- команды ----------
    public void move() { game.act("MOVE"); }
    public void turnLeft() { game.act("LEFT"); }
    public void turnRight() { game.act("RIGHT"); }
    public void take() { game.act("TAKE"); }
    public void drop() { game.act("DROP"); }

    // ---------- район и заказы ----------
    public int[] houses() { return game.houses(); }
    public List<String> orders() { return game.orders(); }
    public List<String> newOrders() { return game.newOrders(); }
    public String houseName() { return game.houseName(); }
    public void goTo(String address) { game.goTo(address); }
}
