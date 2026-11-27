import java.util.Scanner;

public class DigitSumInCode {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String code = scanner.nextLine();
        int sum = 0;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (Character.isDigit(c)) {
                sum += c - '0';
            }
        }
        System.out.println(sum);
    }
}
