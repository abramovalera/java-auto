import java.util.Scanner;

public class LatinSquareCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] square = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                square[i][j] = scanner.nextInt();
            }
        }
        boolean ok = true;
        for (int i = 0; i < n && ok; i++) {
            boolean[] inRow = new boolean[n + 1];
            boolean[] inCol = new boolean[n + 1];
            for (int j = 0; j < n; j++) {
                int a = square[i][j];
                int b = square[j][i];
                if (a < 1 || a > n || inRow[a] || b < 1 || b > n || inCol[b]) {
                    ok = false;
                    break;
                }
                inRow[a] = true;
                inCol[b] = true;
            }
        }
        if (ok) {
            System.out.println("Решено верно");
        } else {
            System.out.println("Есть ошибка");
        }
    }
}
