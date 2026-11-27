import java.util.Arrays;
import java.util.Scanner;

public class ShelfZerosToEnd {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] shelf = new int[n];
        for (int i = 0; i < n; i++) {
            shelf[i] = scanner.nextInt();
        }
        int pos = 0;
        for (int i = 0; i < n; i++) {
            if (shelf[i] != 0) {
                shelf[pos] = shelf[i];
                pos++;
            }
        }
        for (int i = pos; i < n; i++) {
            shelf[i] = 0;
        }
        System.out.println(Arrays.toString(shelf));
    }
}
