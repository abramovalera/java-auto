import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class MissingTicketNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim());
        String line = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        int expected = IntStream.rangeClosed(1, n).sum();
        int collected = Arrays.stream(line.split(" "))
                .filter(s -> !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .sum();
        System.out.println("Не пришёл билет №" + (expected - collected));
    }
}
