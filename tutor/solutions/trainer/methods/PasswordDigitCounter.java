import java.util.Scanner;

public class PasswordDigitCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String password = scanner.next();
        int count = 0;
        for (int i = 0; i < password.length(); i++) {
            if (isDigit(password.charAt(i))) {
                count++;
            }
        }
        System.out.println("Цифр в пароле: " + count);
    }

    static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
