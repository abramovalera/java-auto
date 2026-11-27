import java.util.Arrays;
import java.util.Scanner;

public class MostOrderedPizza {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] orders = new int[n];
        for (int i = 0; i < n; i++) {
            orders[i] = scanner.nextInt();
        }
        Arrays.sort(orders);
        int bestPizza = orders[0];
        int bestCount = 1;
        int count = 1;
        for (int i = 1; i < n; i++) {
            if (orders[i] == orders[i - 1]) {
                count++;
            } else {
                count = 1;
            }
            if (count > bestCount) {
                bestCount = count;
                bestPizza = orders[i];
            }
        }
        System.out.println("Пицца " + bestPizza + ": " + bestCount + " раз");
    }
}
