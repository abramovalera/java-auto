import java.util.Scanner;

public class AverageMarkRounded {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += scanner.nextInt();
        }
        if (n == 0) {
            System.out.println("Оценок нет");
        } else {
            double average = (double) sum / n;
            System.out.println(Math.round(average * 10) / 10.0);
        }
    }
}
