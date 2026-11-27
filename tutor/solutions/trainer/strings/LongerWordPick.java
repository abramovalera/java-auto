import java.util.Scanner;

public class LongerWordPick {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String first = scanner.nextLine();
        String second = scanner.nextLine();
        if (first.length() > second.length()) {
            System.out.println(first);
        } else if (second.length() > first.length()) {
            System.out.println(second);
        } else {
            System.out.println("одинаковой длины");
        }
    }
}
