import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Scanner;

public class TemperatureSpread {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IntSummaryStatistics stats = Arrays.stream(scanner.nextLine().trim().split(" "))
                .mapToInt(Integer::parseInt)
                .summaryStatistics();
        System.out.println("Минимум: " + stats.getMin());
        System.out.println("Максимум: " + stats.getMax());
        System.out.println("Перепад: " + (stats.getMax() - stats.getMin()));
    }
}
