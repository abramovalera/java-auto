import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProfitDaysBrackets {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String result = Arrays.stream(scanner.nextLine().trim().split(" "))
                .mapToInt(Integer::parseInt)
                .filter(p -> p > 0)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println(result);
    }
}
