import java.util.Arrays;
import java.util.Scanner;

public class FillGardenRow {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int seed = scanner.nextInt();
        int[] row = new int[n];
        Arrays.fill(row, seed);
        System.out.println(Arrays.toString(row));
    }
}
