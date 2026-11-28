public class CourierPart1 {

    // Игра вызывает этот метод на каждом ходу: смотрим на датчики и даём роботу ровно одну команду.
    public static void step(CourierRobot robot) {
        if (robot.parcelHere() && !robot.carrying()) {
            robot.take();
        } else if (robot.atTarget() && robot.carrying()) {
            robot.drop();
        } else if (robot.pitAhead()) {
            robot.jump();
        } else if (!robot.wallAhead()) {
            robot.move();
        } else if (!robot.wallLeft()) {
            robot.turnLeft();
        } else {
            robot.turnRight();
        }
    }
}
