import java.util.Scanner;

public class SwitchCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        String op = scanner.next();
        int b = scanner.nextInt();
        if ((op.equals("/") || op.equals("%")) && b == 0) {
            System.out.println("Делить на ноль нельзя");
        } else {
            switch (op) {
                case "+" -> System.out.println(a + " + " + b + " = " + (a + b));
                case "-" -> System.out.println(a + " - " + b + " = " + (a - b));
                case "*" -> System.out.println(a + " * " + b + " = " + (a * b));
                case "/" -> System.out.println(a + " / " + b + " = " + (a / b));
                case "%" -> System.out.println(a + " % " + b + " = " + (a % b));
                default -> System.out.println("Неизвестная операция: " + op);
            }
        }
    }
}
