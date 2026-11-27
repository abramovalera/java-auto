import java.util.Arrays;
import java.util.Scanner;

public class MergeSortedQueues {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int m = scanner.nextInt();
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = scanner.nextInt();
        }
        int[] merged = new int[n + m];
        int i = 0;
        int j = 0;
        int pos = 0;
        while (i < n && j < m) {
            if (a[i] <= b[j]) {
                merged[pos] = a[i];
                i++;
            } else {
                merged[pos] = b[j];
                j++;
            }
            pos++;
        }
        while (i < n) {
            merged[pos] = a[i];
            i++;
            pos++;
        }
        while (j < m) {
            merged[pos] = b[j];
            j++;
            pos++;
        }
        System.out.println(Arrays.toString(merged));
    }
}
