public class CourierMiddle {

    public static void run(CourierRobot robot) {
        int length = driveToWall(robot);
        turnAround(robot);
        driveSteps(robot, length - length / 2);
        robot.drop();
    }

    static int driveToWall(CourierRobot robot) {
        int steps = 0;
        while (!robot.wallAhead()) {
            robot.move();
            steps++;
        }
        return steps;
    }

    static void turnAround(CourierRobot robot) {
        robot.turnLeft();
        robot.turnLeft();
    }

    static void driveSteps(CourierRobot robot, int steps) {
        for (int i = 0; i < steps; i++) {
            robot.move();
        }
    }
}
