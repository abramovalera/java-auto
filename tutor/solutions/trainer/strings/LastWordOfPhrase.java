import java.util.Scanner;

public class LastWordOfPhrase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String phrase = scanner.nextLine().strip();
        String last = phrase.substring(phrase.lastIndexOf(' ') + 1);
        System.out.println(last + " " + last.length());
    }
}
