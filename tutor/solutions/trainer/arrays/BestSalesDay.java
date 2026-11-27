import java.util.Scanner;

public class BestSalesDay {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] sales = new int[n];
        for (int i = 0; i < n; i++) {
            sales[i] = scanner.nextInt();
        }
        int best = 0;
        for (int i = 1; i < n; i++) {
            if (sales[i] > sales[best]) {
                best = i;
            }
        }
        System.out.println("День " + (best + 1) + ": " + sales[best] + " руб.");
    }
}
