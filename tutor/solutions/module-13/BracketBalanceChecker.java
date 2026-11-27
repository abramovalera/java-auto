import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class BracketBalanceChecker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        System.out.println(isBalanced(line) ? "YES" : "NO");
    }

    static boolean isBalanced(String line) {
        Deque<Character> stack = new ArrayDeque<>();   // открытые, ещё не закрытые скобки
        for (char c : line.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty()) {
                    return false;                      // закрываем то, что не открывали
                }
                char open = stack.pop();
                if (!matches(open, c)) {
                    return false;                      // закрыли не той скобкой
                }
            }
        }
        return stack.isEmpty();                        // всё открытое должно быть закрыто
    }

    static boolean matches(char open, char close) {
        return (open == '(' && close == ')')
                || (open == '[' && close == ']')
                || (open == '{' && close == '}');
    }
}
