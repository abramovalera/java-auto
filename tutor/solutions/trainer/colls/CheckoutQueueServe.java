import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class CheckoutQueueServe {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Deque<String> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            queue.offer(sc.next());
        }
        int k = sc.nextInt();
        for (int i = 0; i < k; i++) {
            System.out.println("Обслужен: " + queue.poll());
        }
        if (queue.isEmpty()) {
            System.out.println("Очередь пуста");
        } else {
            System.out.println("Следующий: " + queue.peek());
        }
    }
}
