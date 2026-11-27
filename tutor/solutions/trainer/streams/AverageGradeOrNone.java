import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.Scanner;

public class AverageGradeOrNone {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        OptionalDouble avg = Arrays.stream(line.split(" "))
                .filter(s -> !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .average();
        if (avg.isPresent()) {
            System.out.println("Средний балл: " + Math.round(avg.getAsDouble() * 100) / 100.0);
        } else {
            System.out.println("нет данных");
        }
    }
}
