import java.util.Scanner;

public class CountDigitsInPassword {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String password = scanner.next();
        char[] chars = password.toCharArray();
        int digits = 0;
        for (char c : chars) {
            if (c >= '0' && c <= '9') {
                digits++;
            }
        }
        System.out.println("Цифр: " + digits);
    }
}
