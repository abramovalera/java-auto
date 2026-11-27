import java.util.Arrays;
import java.util.Scanner;

public class DebtDetector {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean hasDebt = Arrays.stream(scanner.nextLine().trim().split(" "))
                .mapToInt(Integer::parseInt)
                .anyMatch(b -> b < 0);
        System.out.println(hasDebt ? "Есть долг" : "Долгов нет");
    }
}
