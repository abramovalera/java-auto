import java.util.Scanner;

public class PocketCalculatorOps {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double a = scanner.nextDouble();
        String op = scanner.next();
        double b = scanner.nextDouble();
        switch (op) {
            case "+" -> System.out.println(a + b);
            case "-" -> System.out.println(a - b);
            case "*" -> System.out.println(a * b);
            case "/" -> {
                if (b == 0) {
                    System.out.println("Делить на ноль нельзя");
                } else {
                    System.out.println(a / b);
                }
            }
            default -> System.out.println("Неизвестная операция: " + op);
        }
    }
}
