import java.util.Scanner;

public class SymmetricDistanceTable {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = scanner.nextInt();
            }
        }
        int badRow = -1;
        int badCol = -1;
        for (int i = 0; i < n && badRow == -1; i++) {
            for (int j = 0; j < n; j++) {
                if (dist[i][j] != dist[j][i]) {
                    badRow = i;
                    badCol = j;
                    break;
                }
            }
        }
        if (badRow == -1) {
            System.out.println("Таблица верна");
        } else {
            System.out.println("Ошибка: строка " + (badRow + 1) + ", столбец " + (badCol + 1));
        }
    }
}
