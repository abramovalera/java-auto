import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class GiftPairsWithSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] prices = Arrays.stream(scanner.nextLine().trim().split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
        int k = scanner.nextInt();
        long pairs = IntStream.range(0, prices.length)
                .mapToLong(i -> IntStream.range(i + 1, prices.length)
                        .filter(j -> prices[i] + prices[j] == k)
                        .count())
                .sum();
        System.out.println("Пар: " + pairs);
    }
}
