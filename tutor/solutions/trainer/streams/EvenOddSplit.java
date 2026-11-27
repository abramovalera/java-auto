import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EvenOddSplit {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<Boolean, List<Integer>> parts = Arrays.stream(scanner.nextLine().trim().split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.partitioningBy(x -> x % 2 == 0));
        System.out.println("Чётные: " + parts.get(true));
        System.out.println("Нечётные: " + parts.get(false));
    }
}
