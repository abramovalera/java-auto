import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;

public class FormulaBracketsValidator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String formula = sc.hasNextLine() ? sc.nextLine() : "";
        Map<Character, Character> pairs = Map.of(')', '(', ']', '[', '}', '{');
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (pairs.containsKey(c)) {
                if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                    System.out.println("Ошибка в позиции " + (i + 1));
                    return;
                }
            }
        }
        if (stack.isEmpty()) {
            System.out.println("Скобки в порядке");
        } else {
            System.out.println("Не закрыто скобок: " + stack.size());
        }
    }
}
