import java.util.Scanner;

public class CourierFloorsTravel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int current = 1;
        int total = 0;
        for (int i = 0; i < n; i++) {
            int floor = scanner.nextInt();
            total += Math.abs(floor - current);
            current = floor;
        }
        System.out.println(total);
    }
}
