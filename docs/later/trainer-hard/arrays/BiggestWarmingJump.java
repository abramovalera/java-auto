import java.util.Scanner;

public class BiggestWarmingJump {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] temps = new int[n];
        for (int i = 0; i < n; i++) {
            temps[i] = scanner.nextInt();
        }
        int min = temps[0];
        int best = 0;
        for (int j = 1; j < n; j++) {
            if (temps[j] - min > best) {
                best = temps[j] - min;
            }
            if (temps[j] < min) {
                min = temps[j];
            }
        }
        System.out.println("Потепление: " + best);
    }
}
