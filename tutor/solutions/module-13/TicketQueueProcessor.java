import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class TicketQueueProcessor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Queue<String> queue = new ArrayDeque<>();

        while (true) {
            String command = scanner.next();
            if (command.equals("NEW")) {
                queue.offer(scanner.next());
            } else if (command.equals("NEXT")) {
                String client = queue.poll();          // null, если очередь пуста
                if (client == null) {
                    System.out.println("Очередь пуста");
                } else {
                    System.out.println("Обслужен: " + client);
                }
            } else if (command.equals("END")) {
                System.out.println("Осталось в очереди: " + queue.size());
                break;
            }
        }
    }
}
