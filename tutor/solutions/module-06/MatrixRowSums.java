import java.util.Scanner;

public class MatrixRowSums {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        int[][] table = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                table[r][c] = scanner.nextInt();
            }
        }

        int bestSum = 0;
        int bestRow = 0;
        for (int r = 0; r < table.length; r++) {
            int rowSum = 0;
            for (int c = 0; c < table[r].length; c++) {
                rowSum += table[r][c];
            }
            System.out.println("Строка " + (r + 1) + ": " + rowSum);
            if (r == 0 || rowSum > bestSum) {    // первая строка — стартовый рекорд
                bestSum = rowSum;
                bestRow = r;
            }
        }
        System.out.println("Больше всего: строка " + (bestRow + 1));
    }
}
