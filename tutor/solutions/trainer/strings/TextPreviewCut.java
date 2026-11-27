import java.util.Scanner;

public class TextPreviewCut {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        int n = scanner.nextInt();
        if (text.length() <= n) {
            System.out.println(text);
        } else {
            System.out.println(text.substring(0, n) + "...");
        }
    }
}
