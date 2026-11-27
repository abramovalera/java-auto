import java.util.Scanner;

public class FormulaParenthesesCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String formula = scanner.nextLine();
        int open = 0;
        boolean ok = true;
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == '(') {
                open++;
            } else if (c == ')') {
                open--;
                if (open < 0) {
                    ok = false;
                    break;
                }
            }
        }
        if (ok && open == 0) {
            System.out.println("скобки в порядке");
        } else {
            System.out.println("ошибка в скобках");
        }
    }
}
