import java.util.Arrays;
import java.util.Scanner;

public class DistinctStampCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] stamps = new int[n];
        for (int i = 0; i < n; i++) {
            stamps[i] = scanner.nextInt();
        }
        Arrays.sort(stamps);
        int distinct = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || stamps[i] != stamps[i - 1]) {
                distinct++;
            }
        }
        System.out.println("Разных марок: " + distinct);
    }
}
