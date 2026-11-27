import java.util.Scanner;

public class FirstAndLastLetter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        char first = word.charAt(0);
        char last = word.charAt(word.length() - 1);
        System.out.println("первая: " + first + ", последняя: " + last);
    }
}
