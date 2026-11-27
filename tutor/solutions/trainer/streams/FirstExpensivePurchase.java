import java.util.Arrays;
import java.util.Scanner;

public class FirstExpensivePurchase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] parts = scanner.nextLine().trim().split(" ");
        int limit = scanner.nextInt();
        String answer = Arrays.stream(parts)
                .map(Integer::parseInt)
                .filter(p -> p > limit)
                .findFirst()
                .map(p -> "Первая покупка дороже " + limit + ": " + p)
                .orElse("не найдено");
        System.out.println(answer);
    }
}
