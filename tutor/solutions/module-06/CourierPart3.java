public class CourierPart3 {

    public static void run(CourierRobot robot) {
        String[] route = new String[300];
        int count = 0;
        while (!robot.atTarget()) {
            if (!robot.wallAhead()) {
                robot.move();
                route[count] = "MOVE";
            } else if (!robot.wallLeft()) {
                robot.turnLeft();
                route[count] = "LEFT";
            } else {
                robot.turnRight();
                route[count] = "RIGHT";
            }
            count++;
        }
        robot.drop();

        String full = "Маршрут:";
        for (int i = 0; i < count; i++) {
            full += " " + route[i];
        }
        System.out.println(full);

        String brief = "";
        int i = 0;
        while (i < count) {
            int j = i;
            while (j < count && route[j].equals(route[i])) {
                j++;
            }
            if (!brief.isEmpty()) {
                brief += ", ";
            }
            brief += j - i == 1 ? route[i] : route[i] + " x" + (j - i);
            i = j;
        }
        System.out.println("Кратко: " + brief);

        // обратно: развернуться и проиграть маршрут с конца, меняя повороты местами
        robot.turnLeft();
        robot.turnLeft();
        for (int k = count - 1; k >= 0; k--) {
            if (route[k].equals("MOVE")) {
                robot.move();
            } else if (route[k].equals("LEFT")) {
                robot.turnRight();
            } else {
                robot.turnLeft();
            }
        }
    }
}
