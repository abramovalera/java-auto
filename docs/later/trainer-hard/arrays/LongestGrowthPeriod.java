import java.util.Scanner;

public class LongestGrowthPeriod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] subs = new int[n];
        for (int i = 0; i < n; i++) {
            subs[i] = scanner.nextInt();
        }
        int bestLength = 1;
        int bestStart = 0;
        int length = 1;
        int start = 0;
        for (int i = 1; i < n; i++) {
            if (subs[i] > subs[i - 1]) {
                length++;
            } else {
                length = 1;
                start = i;
            }
            if (length > bestLength) {
                bestLength = length;
                bestStart = start;
            }
        }
        System.out.println("Рост " + bestLength + " дн., начиная с дня " + (bestStart + 1));
    }
}
