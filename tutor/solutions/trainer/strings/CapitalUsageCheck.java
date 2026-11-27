import java.util.Scanner;

public class CapitalUsageCheck {
    static boolean isCapitalUsedRight(String word) {
        if (word.equals(word.toUpperCase()) || word.equals(word.toLowerCase())) {
            return true;
        }
        String rest = word.substring(1);
        return Character.isUpperCase(word.charAt(0)) && rest.equals(rest.toLowerCase());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        System.out.println(isCapitalUsedRight(word));
    }
}
