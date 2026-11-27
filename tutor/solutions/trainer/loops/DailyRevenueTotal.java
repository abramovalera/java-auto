import java.util.Scanner;

public class DailyRevenueTotal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long revenue = 0;
        for (int i = 0; i < n; i++) {
            revenue += scanner.nextInt();
        }
        System.out.println("Выручка: " + revenue);
    }
}
