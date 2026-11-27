import java.util.Scanner;

public class CardNumberMask {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String digits = scanner.nextLine().replace(" ", "");
        String lastFour = digits.substring(digits.length() - 4);
        System.out.println("**** **** **** " + lastFour);
    }
}
