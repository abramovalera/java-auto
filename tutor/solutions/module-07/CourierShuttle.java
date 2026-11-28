public class CourierShuttle {

    public static void run(CourierRobot robot) {
        while (robot.parcelHere()) {
            robot.take();
            driveToWall(robot);
            robot.drop();
            turnAround(robot);
            driveToWall(robot);
            turnAround(robot);
        }
    }

    static void driveToWall(CourierRobot robot) {
        while (!robot.wallAhead()) {
            robot.move();
        }
    }

    static void turnAround(CourierRobot robot) {
        robot.turnLeft();
        robot.turnLeft();
    }
}
