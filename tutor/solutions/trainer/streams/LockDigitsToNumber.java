import java.util.Arrays;
import java.util.Scanner;

public class LockDigitsToNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long number = Arrays.stream(scanner.nextLine().trim().split(" "))
                .mapToLong(Long::parseLong)
                .reduce(0L, (acc, d) -> acc * 10 + d);
        System.out.println(number);
    }
}
