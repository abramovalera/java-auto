import java.util.Scanner;

public class OdometerTripDistance {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] readings = new int[n];
        for (int i = 0; i < n; i++) {
            readings[i] = scanner.nextInt();
        }
        System.out.println("Проехали: " + (readings[n - 1] - readings[0]) + " км");
    }
}
