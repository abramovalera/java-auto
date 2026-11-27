import java.util.Scanner;

public class BestScoringStretch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] points = new int[n];
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        int current = points[0];
        int best = points[0];
        for (int i = 1; i < n; i++) {
            current = Math.max(points[i], current + points[i]);
            best = Math.max(best, current);
        }
        System.out.println("Лучший отрезок: " + best);
    }
}
