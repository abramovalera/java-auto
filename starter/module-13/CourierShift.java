import java.util.List;
import java.util.Map;

public class CourierShift {

    // robot.orders()    — все заказы смены (адреса повторяются)
    // robot.goTo(адрес) — автопилот до дома (к каждому дому — один заезд);  robot.drop() — отдать посылку
    public static void run(CourierRobot robot) {
        List<String> orders = robot.orders();

    }
}
