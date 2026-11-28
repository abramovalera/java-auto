public class CourierPart1 {

    // Игра вызывает этот метод на каждом ходу: смотрим на датчики и возвращаем одну команду.
    public static String decide(CourierRobot robot) {
        if (robot.parcelHere() && !robot.carrying()) {
            return "TAKE";
        }
        if (robot.atTarget() && robot.carrying()) {
            return "DROP";
        }
        switch (robot.sign()) {
            case "LEFT":
                return "LEFT";
            case "RIGHT":
                return "RIGHT";
            case "AHEAD":
                return "MOVE";
        }
        if (!robot.wallAhead()) {
            return "MOVE";
        } else if (!robot.wallLeft()) {
            return "LEFT";
        } else {
            return "RIGHT";
        }
    }
}
