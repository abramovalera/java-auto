import java.util.Scanner;

public class ParseTwoDigitsByHand {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String digits = scanner.next();
        int tens = digits.charAt(0) - '0';
        int ones = digits.charAt(1) - '0';
        int number = tens * 10 + ones;
        System.out.println(number + " * 2 = " + number * 2);
    }
}
