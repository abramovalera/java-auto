import java.util.Scanner;

public class ThreeLetterShiftCipher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.next();
        char a = (char) (word.charAt(0) + 1);
        char b = (char) (word.charAt(1) + 1);
        char c = (char) (word.charAt(2) + 1);
        System.out.println("" + a + b + c);
    }
}
