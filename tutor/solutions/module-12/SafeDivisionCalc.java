import java.util.Scanner;

public class SafeDivisionCalc {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        try {
            int result = a / b;
            System.out.println(a + " / " + b + " = " + result);
        } catch (ArithmeticException e) {
            System.out.println("Ошибка: на ноль делить нельзя");
        }
        System.out.println("Калькулятор работает дальше");
    }
}
