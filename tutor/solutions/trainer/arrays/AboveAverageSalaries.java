import java.util.Scanner;

public class AboveAverageSalaries {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] changes = new int[n];
        for (int i = 0; i < n; i++) {
            changes[i] = scanner.nextInt();
        }
        int sum = 0;
        for (int c : changes) {
            sum += c;
        }
        double avg = (double) sum / n;
        int count = 0;
        for (int c : changes) {
            if (c > avg) {
                count++;
            }
        }
        System.out.println("Выше среднего: " + count);
    }
}
