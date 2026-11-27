import java.util.Arrays;
import java.util.Scanner;

public class CombinedRoundScores {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] first = new int[n];
        for (int i = 0; i < n; i++) {
            first[i] = scanner.nextInt();
        }
        int[] second = new int[n];
        for (int i = 0; i < n; i++) {
            second[i] = scanner.nextInt();
        }
        int[] total = new int[n];
        for (int i = 0; i < n; i++) {
            total[i] = first[i] + second[i];
        }
        System.out.println(Arrays.toString(total));
    }
}
