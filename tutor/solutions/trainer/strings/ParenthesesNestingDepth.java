import java.util.Scanner;

public class ParenthesesNestingDepth {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        int open = 0;
        int depth = 0;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '(') {
                open++;
                depth = Math.max(depth, open);
            } else if (c == ')') {
                open--;
            }
        }
        System.out.println(depth);
    }
}
