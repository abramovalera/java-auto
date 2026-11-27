import java.util.Scanner;

public class HighestPointOnMap {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int r = scanner.nextInt();
        int c = scanner.nextInt();
        int[][] map = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                map[i][j] = scanner.nextInt();
            }
        }
        int bestRow = 0;
        int bestCol = 0;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (map[i][j] > map[bestRow][bestCol]) {
                    bestRow = i;
                    bestCol = j;
                }
            }
        }
        System.out.println("Высота " + map[bestRow][bestCol] + ": строка " + (bestRow + 1) + ", столбец " + (bestCol + 1));
    }
}
