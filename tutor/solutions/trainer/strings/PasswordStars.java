import java.util.Scanner;

public class PasswordStars {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        System.out.println("Пароль: " + "*".repeat(password.length()));
    }
}
