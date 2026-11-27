import java.util.Arrays;
import java.util.Scanner;

public class WarmDaysCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();
        long warm = Arrays.stream(line.split(" "))
                .mapToInt(Integer::parseInt)
                .filter(t -> t > 0)
                .count();
        System.out.println("Тёплых дней: " + warm);
    }
}
