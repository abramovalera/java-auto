import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.Scanner;

public class AverageGradeStream {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        OptionalDouble average = Arrays.stream(line.trim().split("\s+"))
                .filter(s -> !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .average();

        if (average.isPresent()) {
            System.out.println("Средний балл: " + String.format("%.2f", average.getAsDouble()));
        } else {
            System.out.println("Оценок нет");
        }
    }
}
