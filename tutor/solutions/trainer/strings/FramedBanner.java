import java.util.Scanner;

public class FramedBanner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine().strip();
        String border = "*".repeat(text.length() + 4);
        System.out.println(border);
        System.out.println("* " + text + " *");
        System.out.println(border);
    }
}
