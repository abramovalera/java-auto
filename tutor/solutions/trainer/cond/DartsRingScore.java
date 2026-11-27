import java.util.Scanner;

public class DartsRingScore {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        int square = x * x + y * y;
        int score;
        if (square <= 10 * 10) {
            score = 10;
        } else if (square <= 50 * 50) {
            score = 5;
        } else if (square <= 100 * 100) {
            score = 1;
        } else {
            score = 0;
        }
        System.out.println("Очки: " + score);
    }
}
