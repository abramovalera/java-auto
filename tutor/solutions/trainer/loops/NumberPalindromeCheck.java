import java.util.Scanner;

public class NumberPalindromeCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int rest = n;
        long reversed = 0;
        while (rest > 0) {
            reversed = reversed * 10 + rest % 10;
            rest /= 10;
        }
        if (reversed == n) {
            System.out.println("Палиндром");
        } else {
            System.out.println("Не палиндром");
        }
    }
}
