import java.util.Scanner;

public class BacteriaColonyNextStep {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int r = scanner.nextInt();
        int c = scanner.nextInt();
        char[][] dish = new char[r][];
        for (int i = 0; i < r; i++) {
            dish[i] = scanner.next().toCharArray();
        }
        char[][] next = new char[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                int alive = 0;
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        int ni = i + di;
                        int nj = j + dj;
                        if ((di != 0 || dj != 0) && ni >= 0 && ni < r && nj >= 0 && nj < c && dish[ni][nj] == '#') {
                            alive++;
                        }
                    }
                }
                if (dish[i][j] == '#') {
                    next[i][j] = (alive == 2 || alive == 3) ? '#' : '.';
                } else {
                    next[i][j] = (alive == 3) ? '#' : '.';
                }
            }
        }
        for (char[] row : next) {
            System.out.println(row);
        }
    }
}
