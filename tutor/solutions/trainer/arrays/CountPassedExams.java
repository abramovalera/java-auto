import java.util.Scanner;

public class CountPassedExams {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] scores = new int[n];
        for (int i = 0; i < n; i++) {
            scores[i] = scanner.nextInt();
        }
        int passed = 0;
        for (int score : scores) {
            if (score >= 60) {
                passed++;
            }
        }
        System.out.println("Сдали: " + passed + " из " + n);
    }
}
