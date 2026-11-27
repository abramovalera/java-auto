import java.util.Scanner;

public class PalindromeCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        String reversed = new StringBuilder(word).reverse().toString();
        System.out.println(word.equals(reversed));
    }
}
