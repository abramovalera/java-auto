import java.util.Scanner;

public class WordStaircase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        for (int i = 1; i <= word.length(); i++) {
            System.out.println(word.substring(0, i));
        }
    }
}
