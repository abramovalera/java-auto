public class CourierHouses {

    public static void run(CourierRobot robot) {
        int[] houses = robot.houses();
        int position = 0;
        for (int house : houses) {
            driveSteps(robot, house - position);
            position = house;
            deliverLeft(robot);
        }
    }

    static void driveSteps(CourierRobot robot, int steps) {
        for (int i = 0; i < steps; i++) {
            robot.move();
        }
    }

    static void deliverLeft(CourierRobot robot) {
        robot.turnLeft();
        robot.move();
        robot.drop();
        robot.turnLeft();
        robot.turnLeft();
        robot.move();
        robot.turnLeft();
    }
}
