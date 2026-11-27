import java.util.Scanner;

public class LetterOccurrences {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine().toLowerCase();
        char letter = Character.toLowerCase(scanner.nextLine().charAt(0));
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == letter) {
                count++;
            }
        }
        System.out.println(count);
    }
}
