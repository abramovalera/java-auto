import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Эталон m15-t08 (Мастерская): results.csv → сводка в summary.txt → печать сводки из файла.
public class TestResultsReport {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<String> rows = new ArrayList<>();
        rows.add("test;status;ms");
        for (int i = 0; i < n; i++) {
            rows.add(sc.nextLine());
        }
        Path results = Path.of("results.csv");
        Files.write(results, rows);

        int total = 0;
        int passed = 0;
        int failed = 0;
        int skipped = 0;
        long time = 0;
        long slowestMs = -1;
        String slowest = null;
        List<String> failedNames = new ArrayList<>();

        List<String> lines = Files.readAllLines(results);
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) {
                continue;
            }
            String[] c = lines.get(i).split(";");
            String name = c[0].trim();
            String status = c[1].trim().toUpperCase();
            long ms = Long.parseLong(c[2].trim());
            total++;
            time += ms;
            if (status.equals("PASSED")) {
                passed++;
            } else if (status.equals("FAILED")) {
                failed++;
                failedNames.add(name);
            } else if (status.equals("SKIPPED")) {
                skipped++;
            }
            if (ms > slowestMs) {
                slowestMs = ms;
                slowest = name;
            }
        }
        failedNames.sort(null);

        List<String> summary = new ArrayList<>();
        summary.add("Всего тестов: " + total);
        summary.add("PASSED: " + passed);
        summary.add("FAILED: " + failed);
        summary.add("SKIPPED: " + skipped);
        summary.add("Общее время: " + time + " мс");
        summary.add("Самый медленный: " + (slowest == null ? "нет" : slowest + " (" + slowestMs + " мс)"));
        summary.add("Упали: " + (failedNames.isEmpty() ? "нет" : String.join(", ", failedNames)));

        Path summaryFile = Path.of("summary.txt");
        Files.write(summaryFile, summary);
        System.out.print(Files.readString(summaryFile));
    }
}
