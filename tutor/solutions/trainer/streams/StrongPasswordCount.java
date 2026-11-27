import java.util.Arrays;
import java.util.Scanner;

public class StrongPasswordCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] passwords = scanner.nextLine().trim().split(" ");
        long strong = Arrays.stream(passwords)
                .filter(p -> p.length() >= 8
                        && p.chars().anyMatch(Character::isDigit)
                        && p.chars().anyMatch(Character::isUpperCase))
                .count();
        System.out.println("Надёжных: " + strong + " из " + passwords.length);
    }
}
