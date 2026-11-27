import java.util.Scanner;

public class MissingCardNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        boolean[] seen = new boolean[n + 1];
        for (int i = 0; i < n - 1; i++) {
            seen[scanner.nextInt()] = true;
        }
        for (int k = 1; k <= n; k++) {
            if (!seen[k]) {
                System.out.println(k);
                break;
            }
        }
    }
}
