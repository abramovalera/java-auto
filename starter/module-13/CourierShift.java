import java.util.List;
import java.util.Map;

public class CourierShift {

    // Шпаргалка по командам — README.md в этой же папке: открой его второй вкладкой рядом с кодом
    // (правой кнопкой по вкладке README.md → Split Right).

    // robot.orders()    — все заказы смены (адреса повторяются)
    // robot.goTo(адрес) — автопилот до дома (к каждому дому — один заезд);  robot.drop() — отдать посылку
    public static void run(CourierRobot robot) {
        List<String> orders = robot.orders();

    }
}
