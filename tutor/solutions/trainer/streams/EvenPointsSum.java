import java.util.Arrays;
import java.util.Scanner;

public class EvenPointsSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sum = Arrays.stream(scanner.nextLine().trim().split(" "))
                .mapToInt(Integer::parseInt)
                .filter(x -> x % 2 == 0)
                .sum();
        System.out.println("Сумма чётных: " + sum);
    }
}
