import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class SafeClickCounter {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        int threadCount = sc.nextInt();
        int clicksPerThread = sc.nextInt();

        AtomicInteger clicks = new AtomicInteger();
        Thread[] users = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            users[i] = new Thread(() -> {
                for (int k = 0; k < clicksPerThread; k++) {
                    clicks.incrementAndGet();
                }
            });
            users[i].start();
        }
        for (Thread user : users) {
            user.join();
        }
        System.out.println("Потоков: " + threadCount);
        System.out.println("Кликов: " + clicks.get());
    }
}
