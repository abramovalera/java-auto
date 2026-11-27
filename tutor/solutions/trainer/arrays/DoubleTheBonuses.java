import java.util.Arrays;
import java.util.Scanner;

public class DoubleTheBonuses {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] bonuses = new int[n];
        for (int i = 0; i < n; i++) {
            bonuses[i] = scanner.nextInt();
        }
        for (int i = 0; i < n; i++) {
            bonuses[i] = bonuses[i] * 2;
        }
        System.out.println(Arrays.toString(bonuses));
    }
}
