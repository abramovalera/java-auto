import java.util.Scanner;

public class DiagonalSumsOfSquare {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = scanner.nextInt();
            }
        }
        int main = 0;
        int anti = 0;
        for (int i = 0; i < n; i++) {
            main += tiles[i][i];
            anti += tiles[i][n - 1 - i];
        }
        System.out.println("Главная: " + main);
        System.out.println("Побочная: " + anti);
    }
}
