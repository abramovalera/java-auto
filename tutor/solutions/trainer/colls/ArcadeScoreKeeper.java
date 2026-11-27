import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class ArcadeScoreKeeper {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Deque<Integer> rounds = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            String op = sc.next();
            if (op.equals("+")) {
                int last = rounds.pop();
                int prev = rounds.peek();
                rounds.push(last);
                rounds.push(last + prev);
            } else if (op.equals("D")) {
                rounds.push(rounds.peek() * 2);
            } else if (op.equals("C")) {
                rounds.pop();
            } else {
                rounds.push(Integer.parseInt(op));
            }
        }
        int total = 0;
        for (int r : rounds) {
            total += r;
        }
        System.out.println("Итого: " + total);
    }
}
