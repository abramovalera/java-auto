import java.util.Scanner;

public class SpiralMuseumRoute {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int r = scanner.nextInt();
        int c = scanner.nextInt();
        int[][] halls = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                halls[i][j] = scanner.nextInt();
            }
        }
        int top = 0;
        int bottom = r - 1;
        int left = 0;
        int right = c - 1;
        while (top <= bottom && left <= right) {
            for (int j = left; j <= right; j++) {
                System.out.print(halls[top][j] + " ");
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                System.out.print(halls[i][right] + " ");
            }
            right--;
            if (top <= bottom) {
                for (int j = right; j >= left; j--) {
                    System.out.print(halls[bottom][j] + " ");
                }
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    System.out.print(halls[i][left] + " ");
                }
                left++;
            }
        }
        System.out.println();
    }
}
