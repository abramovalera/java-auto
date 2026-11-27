import java.util.Arrays;
import java.util.Scanner;

public class MedianDeliveryTime {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] times = new int[n];
        for (int i = 0; i < n; i++) {
            times[i] = scanner.nextInt();
        }
        Arrays.sort(times);
        double median;
        if (n % 2 == 1) {
            median = times[n / 2];
        } else {
            median = (times[n / 2 - 1] + times[n / 2]) / 2.0;
        }
        System.out.println("Медиана: " + median);
    }
}
