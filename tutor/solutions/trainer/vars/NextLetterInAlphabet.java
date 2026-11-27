import java.util.Scanner;

public class NextLetterInAlphabet {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char symbol = scanner.next().charAt(0);
        char next = (char) (symbol + 1);
        System.out.println("После " + symbol + " идёт " + next);
    }
}
