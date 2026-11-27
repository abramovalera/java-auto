import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelArraySum {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = sc.nextInt();
        }

        ExecutorService pool = Executors.newFixedThreadPool(k);
        List<Future<Long>> parts = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int from = i * n / k;
            int to = (i + 1) * n / k;
            parts.add(pool.submit(() -> {
                long sum = 0;
                for (int j = from; j < to; j++) {
                    sum += numbers[j];
                }
                return sum;
            }));
        }

        long total = 0;
        for (int i = 0; i < parts.size(); i++) {
            long partSum = parts.get(i).get();   // ждём результат именно этой части
            System.out.println("Часть " + (i + 1) + ": " + partSum);
            total += partSum;
        }
        pool.shutdown();
        System.out.println("Итого: " + total);
    }
}
