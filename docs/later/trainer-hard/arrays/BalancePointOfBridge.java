import java.util.Scanner;

public class BalancePointOfBridge {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] weights = new int[n];
        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }
        int total = 0;
        for (int w : weights) {
            total += w;
        }
        int left = 0;
        int answer = -1;
        for (int i = 0; i < n; i++) {
            int right = total - left - weights[i];
            if (left == right) {
                answer = i;
                break;
            }
            left += weights[i];
        }
        System.out.println(answer);
    }
}
