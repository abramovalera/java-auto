import java.util.Scanner;

public class MaxScoreInGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] scores = new int[n];
        for (int i = 0; i < n; i++) {
            scores[i] = scanner.nextInt();
        }
        int max = scores[0];
        for (int score : scores) {
            if (score > max) {
                max = score;
            }
        }
        System.out.println("Рекорд: " + max);
    }
}
