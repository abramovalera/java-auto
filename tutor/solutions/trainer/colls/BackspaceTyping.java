import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class BackspaceTyping {
    static String typed(String keys) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : keys.toCharArray()) {
            if (c == '#') {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else {
                stack.push(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.insert(0, stack.pop());
        }
        return sb.toString();
    }

    static String show(String text) {
        return text.isEmpty() ? "(пусто)" : text;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = typed(sc.next());
        String b = typed(sc.next());
        System.out.println("Первый: " + show(a));
        System.out.println("Второй: " + show(b));
        System.out.println(a.equals(b) ? "Одинаково" : "По-разному");
    }
}
