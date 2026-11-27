import java.util.Scanner;

public class FirstLetterBadge {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        char first = name.charAt(0);
        System.out.println("Значок: [" + first + "]");
    }
}
