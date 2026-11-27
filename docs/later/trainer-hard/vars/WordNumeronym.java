import java.util.Scanner;

public class WordNumeronym {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.next();
        char first = word.charAt(0);
        char last = word.charAt(word.length() - 1);
        int middle = word.length() - 2;
        System.out.println("" + first + middle + last);
    }
}
