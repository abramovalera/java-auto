import java.util.Scanner;

public class ElevatorFloorDistance {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int from = scanner.nextInt();
        int to = scanner.nextInt();
        System.out.println("Этажей в пути: " + floorsBetween(from, to));
    }

    static int floorsBetween(int from, int to) {
        return Math.abs(from - to);
    }
}
