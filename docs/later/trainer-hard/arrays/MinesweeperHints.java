import java.util.Scanner;

public class MinesweeperHints {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int r = scanner.nextInt();
        int c = scanner.nextInt();
        char[][] field = new char[r][];
        for (int i = 0; i < r; i++) {
            field[i] = scanner.next().toCharArray();
        }
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (field[i][j] == '*') {
                    System.out.print('*');
                    continue;
                }
                int mines = 0;
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        int ni = i + di;
                        int nj = j + dj;
                        if ((di != 0 || dj != 0) && ni >= 0 && ni < r && nj >= 0 && nj < c && field[ni][nj] == '*') {
                            mines++;
                        }
                    }
                }
                System.out.print((char) ('0' + mines));
            }
            System.out.println();
        }
    }
}
