import java.util.Scanner;

public class RpnCalculatorSafe {
    static int evaluate(String expression) throws RpnStackUnderflowException {
        String[] tokens = expression.trim().split("\\s+");
        int[] stack = new int[tokens.length];
        int size = 0;
        for (String token : tokens) {
            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                if (size < 2) {
                    throw new RpnStackUnderflowException("не хватает чисел для " + token);
                }
                size--;
                int b = stack[size];
                size--;
                int a = stack[size];
                int result;
                switch (token) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                    case "*":
                        result = a * b;
                        break;
                    default:
                        result = a / b;
                }
                stack[size] = result;
                size++;
            } else {
                try {
                    stack[size] = Integer.parseInt(token);
                    size++;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("непонятный символ " + token);
                }
            }
        }
        if (size != 1) {
            throw new IllegalArgumentException("лишние числа в конце");
        }
        return stack[0];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; i++) {
            String expression = scanner.nextLine();
            try {
                System.out.println("= " + evaluate(expression));
            } catch (ArithmeticException e) {
                System.out.println("Ошибка: деление на ноль");
            } catch (IllegalArgumentException | RpnStackUnderflowException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}

class RpnStackUnderflowException extends Exception {
    RpnStackUnderflowException(String message) {
        super(message);
    }
}
