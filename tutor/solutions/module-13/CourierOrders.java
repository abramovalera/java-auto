import java.util.ArrayDeque;
import java.util.Queue;

public class CourierOrders {

    public static void run(CourierRobot robot) {
        Queue<String> queue = new ArrayDeque<>(robot.orders());
        while (!queue.isEmpty()) {
            String address = queue.poll();
            robot.goTo(address);
            robot.drop();
            queue.addAll(robot.newOrders());
        }
    }
}
