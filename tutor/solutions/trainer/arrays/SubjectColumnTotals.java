import java.util.Scanner;

public class SubjectColumnTotals {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int r = scanner.nextInt();
        int c = scanner.nextInt();
        int[][] grades = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                grades[i][j] = scanner.nextInt();
            }
        }
        int[] totals = new int[c];
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                totals[j] += grades[i][j];
            }
        }
        int best = 0;
        for (int j = 0; j < c; j++) {
            System.out.print(totals[j] + " ");
            if (totals[j] > totals[best]) {
                best = j;
            }
        }
        System.out.println();
        System.out.println("Лучший предмет: " + (best + 1));
    }
}
