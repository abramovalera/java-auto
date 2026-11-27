import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FailedTestsCollector {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lines.add(sc.nextLine());
        }

        List<String> failed = Collections.synchronizedList(new ArrayList<>());
        ExecutorService workers = Executors.newFixedThreadPool(3);
        for (String line : lines) {
            workers.submit(() -> {
                String[] parts = line.trim().split("\s+");
                if (parts[1].equalsIgnoreCase("FAILED")) {
                    failed.add(parts[0]);
                }
            });
        }
        workers.shutdown();
        workers.awaitTermination(10, TimeUnit.SECONDS);

        List<String> sorted = new ArrayList<>(failed);
        Collections.sort(sorted);
        if (sorted.isEmpty()) {
            System.out.println("Упавших тестов нет");
        } else {
            System.out.println("Упавшие тесты:");
            for (String name : sorted) {
                System.out.println(name);
            }
        }
        System.out.println("Всего упало: " + sorted.size() + " из " + n);
    }
}
