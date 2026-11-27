import java.util.Scanner;

public class AverageCafeCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        double[] checks = new double[n];
        for (int i = 0; i < n; i++) {
            checks[i] = scanner.nextDouble();
        }
        double sum = 0;
        for (double c : checks) {
            sum += c;
        }
        double average = sum / n;
        System.out.println("Средний чек: " + Math.round(average * 100) / 100.0);
    }
}
