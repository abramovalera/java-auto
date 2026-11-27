import java.util.Scanner;

public class LetterPositions {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine().toLowerCase();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(word.charAt(i) - 'a' + 1);
        }
        System.out.println(sb.toString());
    }
}
