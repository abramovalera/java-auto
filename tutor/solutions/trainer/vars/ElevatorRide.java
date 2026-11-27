import java.util.Scanner;

public class ElevatorRide {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int floor = scanner.nextInt();
        int up = scanner.nextInt();
        int down = scanner.nextInt();
        floor = floor + up;
        floor = floor - down;
        System.out.println("Лифт на этаже " + floor);
    }
}
