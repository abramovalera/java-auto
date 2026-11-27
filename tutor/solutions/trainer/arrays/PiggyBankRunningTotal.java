import java.util.Arrays;
import java.util.Scanner;

public class PiggyBankRunningTotal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] money = new int[n];
        for (int i = 0; i < n; i++) {
            money[i] = scanner.nextInt();
        }
        for (int i = 1; i < n; i++) {
            money[i] = money[i - 1] + money[i];
        }
        System.out.println(Arrays.toString(money));
    }
}
