import java.util.Arrays;
import java.util.Scanner;

public class AppendLateRunner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] runners = new int[n];
        for (int i = 0; i < n; i++) {
            runners[i] = scanner.nextInt();
        }
        int late = scanner.nextInt();
        int[] all = Arrays.copyOf(runners, n + 1);
        all[n] = late;
        System.out.println(Arrays.toString(all));
    }
}
