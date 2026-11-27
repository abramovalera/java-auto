import java.util.Scanner;

public class EmailDomainPart {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String email = scanner.nextLine();
        int at = email.indexOf('@');
        System.out.println(email.substring(at + 1).toLowerCase());
    }
}
