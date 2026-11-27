import java.util.Scanner;

public class DailyTemperatureSwing {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] temps = new int[n];
        for (int i = 0; i < n; i++) {
            temps[i] = scanner.nextInt();
        }
        int min = temps[0];
        int max = temps[0];
        for (int t : temps) {
            if (t < min) {
                min = t;
            }
            if (t > max) {
                max = t;
            }
        }
        System.out.println("Минимум: " + min);
        System.out.println("Максимум: " + max);
        System.out.println("Перепад: " + (max - min));
    }
}
