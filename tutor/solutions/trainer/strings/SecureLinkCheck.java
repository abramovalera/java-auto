import java.util.Scanner;

public class SecureLinkCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String link = scanner.nextLine();
        if (link.toLowerCase().startsWith("https://")) {
            System.out.println("защищённая");
        } else {
            System.out.println("небезопасная");
        }
    }
}
