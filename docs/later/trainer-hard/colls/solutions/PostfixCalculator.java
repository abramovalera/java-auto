import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class PostfixCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] tokens = sc.nextLine().trim().split(" ");
        Deque<Integer> stack = new ArrayDeque<>();
        for (String t : tokens) {
            if (t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/")) {
                if (stack.size() < 2) {
                    System.out.println("Ошибка");
                    return;
                }
                int right = stack.pop();
                int left = stack.pop();
                if (t.equals("+")) {
                    stack.push(left + right);
                } else if (t.equals("-")) {
                    stack.push(left - right);
                } else if (t.equals("*")) {
                    stack.push(left * right);
                } else {
                    if (right == 0) {
                        System.out.println("Ошибка");
                        return;
                    }
                    stack.push(left / right);
                }
            } else {
                stack.push(Integer.parseInt(t));
            }
        }
        if (stack.size() != 1) {
            System.out.println("Ошибка");
        } else {
            System.out.println(stack.pop());
        }
    }
}
