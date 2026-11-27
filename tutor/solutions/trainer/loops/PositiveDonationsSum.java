import java.util.Scanner;

public class PositiveDonationsSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int collected = 0;
        int errors = 0;
        for (int i = 0; i < n; i++) {
            int amount = scanner.nextInt();
            if (amount < 0) {
                errors++;
                continue;
            }
            collected += amount;
        }
        System.out.println("Собрано: " + collected + ", ошибок: " + errors);
    }
}
