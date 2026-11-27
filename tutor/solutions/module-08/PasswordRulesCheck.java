import java.util.Scanner;

public class PasswordRulesCheck {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String pass = sc.nextLine();
        boolean longEnough = pass.length() >= 8;
        boolean hasDigit = false;
        boolean hasUpper = false;
        for (char c : pass.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }
        }
        if (longEnough && hasDigit && hasUpper) {
            System.out.println("Пароль надёжный");
        } else {
            if (!longEnough) {
                System.out.println("Слишком короткий: нужно минимум 8 символов");
            }
            if (!hasDigit) {
                System.out.println("Нужна хотя бы одна цифра");
            }
            if (!hasUpper) {
                System.out.println("Нужна хотя бы одна заглавная буква");
            }
        }
    }
}
