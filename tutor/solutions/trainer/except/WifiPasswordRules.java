import java.util.Scanner;

public class WifiPasswordRules {
    static void validate(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("короче 8 символов");
        }
        if (password.contains(" ")) {
            throw new IllegalArgumentException("пробелы нельзя");
        }
        boolean hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                hasDigit = true;
            }
        }
        if (!hasDigit) {
            throw new IllegalArgumentException("нет ни одной цифры");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; i++) {
            String password = scanner.nextLine();
            try {
                validate(password);
                System.out.println("Пароль подходит");
            } catch (IllegalArgumentException e) {
                System.out.println("Не подходит: " + e.getMessage());
            }
        }
    }
}
