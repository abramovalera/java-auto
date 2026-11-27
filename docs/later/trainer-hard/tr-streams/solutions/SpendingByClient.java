import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class SpendingByClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> totals = Arrays.stream(scanner.nextLine().trim().split(" "))
                .map(s -> s.split(":"))
                .collect(Collectors.groupingBy(
                        p -> p[0],
                        TreeMap::new,
                        Collectors.summingInt(p -> Integer.parseInt(p[1]))));
        totals.forEach((name, sum) -> System.out.println(name + ": " + sum));
        int all = totals.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Итого: " + all);
    }
}
