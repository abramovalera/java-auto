import java.util.Scanner;

public class BestKDaysRevenue {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] profit = new int[n];
        for (int i = 0; i < n; i++) {
            profit[i] = scanner.nextInt();
        }
        int k = scanner.nextInt();
        int bestSum = 0;
        int bestStart = -1;
        for (int start = 0; start + k <= n; start++) {
            int sum = 0;
            for (int i = start; i < start + k; i++) {
                sum += profit[i];
            }
            if (bestStart == -1 || sum > bestSum) {
                bestSum = sum;
                bestStart = start;
            }
        }
        System.out.println("Сумма " + bestSum + ", с дня " + (bestStart + 1));
    }
}
