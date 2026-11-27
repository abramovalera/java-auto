import java.util.Scanner;

public class CoinFlipGuess {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String guess = scanner.nextLine();
        String coin = scanner.nextLine();
        if (guess.equals(coin)) {
            System.out.println("Угадал!");
        } else {
            System.out.println("Не угадал. Выпало: " + coin);
        }
    }
}
