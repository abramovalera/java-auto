import java.util.Scanner;

public class GuessNumberAttempts {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int secret = scanner.nextInt();
        int attempts = 0;
        int guess;
        do {
            guess = scanner.nextInt();
            attempts++;
            if (guess < secret) {
                System.out.println("Больше");
            } else if (guess > secret) {
                System.out.println("Меньше");
            }
        } while (guess != secret);
        System.out.println("Угадал!");
        System.out.println("Попыток: " + attempts);
    }
}
