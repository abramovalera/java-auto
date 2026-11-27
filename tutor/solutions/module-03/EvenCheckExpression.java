import java.util.Scanner;

public class EvenCheckExpression {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();
        // Чётное — значит, делится на 2 без остатка. Сравниваем с 0, а не «!= 1»:
        // у отрицательных нечётных остаток равен -1.
        boolean isEven = number % 2 == 0;
        System.out.println(isEven);
    }
}
