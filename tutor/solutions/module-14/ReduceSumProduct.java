import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ReduceSumProduct {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        List<Integer> numbers = Arrays.stream(line.trim().split("\s+"))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();

        int sum = numbers.stream().reduce(0, Integer::sum);
        int product = numbers.stream().reduce(1, (a, b) -> a * b);

        System.out.println("Сумма: " + sum);
        System.out.println("Произведение: " + product);
    }
}
