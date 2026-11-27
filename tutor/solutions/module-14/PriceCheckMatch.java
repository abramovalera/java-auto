import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PriceCheckMatch {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        List<Integer> prices = Arrays.stream(line.trim().split("\s+"))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();

        boolean hasNegative = prices.stream().anyMatch(p -> p < 0);
        boolean allPositive = prices.stream().allMatch(p -> p > 0);
        boolean noFree = prices.stream().noneMatch(p -> p == 0);

        System.out.println("Есть отрицательные: " + (hasNegative ? "да" : "нет"));
        System.out.println("Все цены больше нуля: " + (allPositive ? "да" : "нет"));
        System.out.println("Бесплатных нет: " + (noFree ? "да" : "нет"));
    }
}
