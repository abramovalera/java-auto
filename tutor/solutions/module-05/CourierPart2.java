public class CourierPart2 {

    public static void run(CourierRobot robot) {
        while (!robot.atTarget()) {
            while (robot.parcelHere()) {
                robot.take();
            }
            if (!robot.wallAhead()) {
                robot.move();
            } else if (!robot.wallLeft()) {
                robot.turnLeft();
            } else {
                robot.turnRight();
            }
        }
        int delivered = 0;
        while (robot.carrying()) {
            robot.drop();
            delivered++;
        }
        System.out.println("Доставлено: " + delivered);
    }
}
