import java.util.Scanner;

public class CloserToTargetGuess {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int secret = scanner.nextInt();
        int anya = scanner.nextInt();
        int borya = scanner.nextInt();
        boolean anyaWins = Math.abs(anya - secret) < Math.abs(borya - secret);
        System.out.println(anyaWins);
    }
}
