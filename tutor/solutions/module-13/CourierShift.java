import java.util.Map;
import java.util.TreeMap;

public class CourierShift {

    public static void run(CourierRobot robot) {
        Map<String, Integer> counts = new TreeMap<>();
        for (String address : robot.orders()) {
            counts.put(address, counts.getOrDefault(address, 0) + 1);
        }
        int total = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            robot.goTo(entry.getKey());
            for (int i = 0; i < entry.getValue(); i++) {
                robot.drop();
            }
            total += entry.getValue();
        }
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Всего: " + total);
    }
}
