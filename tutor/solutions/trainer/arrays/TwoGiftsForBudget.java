import java.util.Scanner;

public class TwoGiftsForBudget {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] prices = new int[n];
        for (int i = 0; i < n; i++) {
            prices[i] = scanner.nextInt();
        }
        int budget = scanner.nextInt();
        int first = -1;
        int second = -1;
        for (int i = 0; i < n && first == -1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (prices[i] + prices[j] == budget) {
                    first = i;
                    second = j;
                    break;
                }
            }
        }
        if (first == -1) {
            System.out.println("Не получится");
        } else {
            System.out.println("Подарки " + (first + 1) + " и " + (second + 1));
        }
    }
}
