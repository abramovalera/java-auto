import java.util.Scanner;

public class CollapseRepeatedLetters {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        StringBuilder sb = new StringBuilder();
        sb.append(text.charAt(0));
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1)) {
                sb.append(text.charAt(i));
            }
        }
        System.out.println(sb.toString());
    }
}
