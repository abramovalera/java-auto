import java.util.Scanner;

public class SalePrices {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] prices = readPrices(sc);
        int percent = sc.nextInt();
        int[] sale = applyDiscount(prices, percent);
        printPrices("Было:", prices);
        printPrices("Стало:", sale);
    }

    static int[] readPrices(Scanner sc) {
        int n = sc.nextInt();
        int[] prices = new int[n];
        for (int i = 0; i < n; i++) {
            prices[i] = sc.nextInt();
        }
        return prices;
    }

    static int[] applyDiscount(int[] prices, int percent) {
        int[] result = new int[prices.length];
        for (int i = 0; i < prices.length; i++) {
            result[i] = prices[i] - prices[i] * percent / 100;
        }
        return result;
    }

    static void printPrices(String title, int[] prices) {
        System.out.print(title);
        for (int i = 0; i < prices.length; i++) {
            System.out.print(" " + prices[i]);
        }
        System.out.println();
    }
}
