import java.util.Scanner;

public class ReceiptSumTolerance {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double first = scanner.nextDouble();
        double second = scanner.nextDouble();
        double total = scanner.nextDouble();
        boolean matches = Math.abs(first + second - total) < 0.001;
        System.out.println(matches);
    }
}
