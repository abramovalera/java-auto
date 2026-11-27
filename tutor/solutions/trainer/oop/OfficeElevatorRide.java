import java.util.Scanner;

public class OfficeElevatorRide {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OfficeElevator lift = new OfficeElevator(scanner.nextInt(), scanner.nextInt());
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.println(lift.goTo(scanner.nextInt()));
        }
        System.out.println("Лифт на этаже " + lift.getFloor() + ", проехал этажей: " + lift.getTravelled());
    }
}

class OfficeElevator {
    private final int minFloor;
    private final int maxFloor;
    private int floor = 1;
    private int travelled;

    OfficeElevator(int minFloor, int maxFloor) {
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
    }

    String goTo(int target) {
        if (target < minFloor || target > maxFloor) {
            return "Этажа " + target + " нет";
        }
        if (target == floor) {
            return "Уже на этаже " + target;
        }
        String route = "Еду: " + floor + " -> " + target;
        travelled += Math.abs(target - floor);
        floor = target;
        return route;
    }

    int getFloor() {
        return floor;
    }

    int getTravelled() {
        return travelled;
    }
}
