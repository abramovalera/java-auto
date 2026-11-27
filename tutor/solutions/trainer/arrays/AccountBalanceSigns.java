import java.util.Scanner;

public class AccountBalanceSigns {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] balances = new int[n];
        for (int i = 0; i < n; i++) {
            balances[i] = scanner.nextInt();
        }
        int plus = 0;
        int minus = 0;
        int zero = 0;
        for (int b : balances) {
            if (b > 0) {
                plus++;
            } else if (b < 0) {
                minus++;
            } else {
                zero++;
            }
        }
        System.out.println("В плюсе: " + plus);
        System.out.println("В минусе: " + minus);
        System.out.println("На нуле: " + zero);
    }
}
