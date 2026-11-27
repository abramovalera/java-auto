import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class PlateStackTop {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Deque<String> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            stack.push(sc.next());
        }
        int k = sc.nextInt();
        for (int i = 0; i < k; i++) {
            stack.pop();
        }
        if (stack.isEmpty()) {
            System.out.println("Тарелок не осталось");
        } else {
            System.out.println("Наверху: " + stack.peek());
            System.out.println("Осталось: " + stack.size());
        }
    }
}
