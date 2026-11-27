import java.util.Scanner;

public class SpellWordWithDashes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.next();
        for (int i = 0; i < word.length(); i++) {
            if (i > 0) {
                System.out.print("-");
            }
            System.out.print(word.charAt(i));
        }
        System.out.println();
    }
}
