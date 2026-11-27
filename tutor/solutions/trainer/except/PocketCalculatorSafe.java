import java.util.Scanner;

public class PocketCalculatorSafe {
    static int calculate(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Пример пишут так: 2 + 3");
        }
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[2]);
        String op = parts[1];
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            default:
                throw new IllegalArgumentException("Не знаю операцию: " + op);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            System.out.println("Ответ: " + calculate(line));
        } catch (NumberFormatException e) {
            System.out.println("Числа должны быть целыми");
        } catch (ArithmeticException e) {
            System.out.println("На ноль делить нельзя");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
