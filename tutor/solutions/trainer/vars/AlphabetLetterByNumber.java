import java.util.Scanner;

public class AlphabetLetterByNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        char letter = (char) ('A' + n - 1);
        System.out.println("Буква №" + n + ": " + letter);
    }
}
