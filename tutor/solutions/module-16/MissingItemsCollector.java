import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MissingItemsCollector {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lines.add(sc.nextLine());
        }

        List<String> missing = Collections.synchronizedList(new ArrayList<>());
        ExecutorService workers = Executors.newFixedThreadPool(3);
        for (String line : lines) {
            workers.submit(() -> {
                String[] parts = line.trim().split("\s+");
                if (parts[1].equals("MISSING")) {
                    missing.add(parts[0]);
                }
            });
        }
        workers.shutdown();
        workers.awaitTermination(10, TimeUnit.SECONDS);

        List<String> sorted = new ArrayList<>(missing);
        Collections.sort(sorted);
        if (sorted.isEmpty()) {
            System.out.println("Недостачи нет");
        } else {
            System.out.println("Не хватает товаров:");
            for (String name : sorted) {
                System.out.println(name);
            }
        }
        System.out.println("Всего не хватает: " + sorted.size() + " из " + n);
    }
}
