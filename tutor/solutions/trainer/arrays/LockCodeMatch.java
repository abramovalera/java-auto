import java.util.Arrays;
import java.util.Scanner;

public class LockCodeMatch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] secret = new int[n];
        for (int i = 0; i < n; i++) {
            secret[i] = scanner.nextInt();
        }
        int m = scanner.nextInt();
        int[] entered = new int[m];
        for (int i = 0; i < m; i++) {
            entered[i] = scanner.nextInt();
        }
        if (Arrays.equals(secret, entered)) {
            System.out.println("Открыто");
        } else {
            System.out.println("Неверный код");
        }
    }
}
