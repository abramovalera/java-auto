import java.util.Scanner;

public class BranchRowTotals {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int r = scanner.nextInt();
        int c = scanner.nextInt();
        int[][] profit = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                profit[i][j] = scanner.nextInt();
            }
        }
        for (int i = 0; i < r; i++) {
            int sum = 0;
            for (int j = 0; j < c; j++) {
                sum += profit[i][j];
            }
            System.out.println("Филиал " + (i + 1) + ": " + sum);
        }
    }
}
