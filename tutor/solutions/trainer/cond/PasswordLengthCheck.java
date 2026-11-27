import java.util.Scanner;

public class PasswordLengthCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        int length = password.length();
        if (length >= 8) {
            System.out.println("Пароль подходит");
        } else {
            System.out.println("Слишком короткий, не хватает символов: " + (8 - length));
        }
    }
}
