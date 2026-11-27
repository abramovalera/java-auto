import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

record ExamScoreEntry(String name, int score) { }

public class ExamPassFailReport {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] parts = scanner.nextLine().trim().split(" ");
        int pass = scanner.nextInt();
        Map<Boolean, String> report = Arrays.stream(parts)
                .map(s -> s.split(":"))
                .map(p -> new ExamScoreEntry(p[0], Integer.parseInt(p[1])))
                .collect(Collectors.partitioningBy(
                        e -> e.score() >= pass,
                        Collectors.mapping(ExamScoreEntry::name, Collectors.joining(", "))));
        String passed = report.get(true);
        String failed = report.get(false);
        System.out.println("Сдали: " + (passed.isEmpty() ? "никто" : passed));
        System.out.println("Не сдали: " + (failed.isEmpty() ? "никто" : failed));
    }
}
