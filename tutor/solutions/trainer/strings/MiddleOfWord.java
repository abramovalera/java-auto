import java.util.Scanner;

public class MiddleOfWord {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        int mid = word.length() / 2;
        if (word.length() % 2 == 0) {
            System.out.println(word.substring(mid - 1, mid + 1));
        } else {
            System.out.println(word.substring(mid, mid + 1));
        }
    }
}
