import java.util.Scanner;

public class DigitSumRecursion {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int sum = digitSum(n);
        System.out.println("Сумма цифр: " + sum);
        int root = sum;
        while (root >= 10) {
            root = digitSum(root);
        }
        System.out.println("Цифровой корень: " + root);
    }

    static int digitSum(int n) {
        if (n < 10) {
            return n;
        }
        return n % 10 + digitSum(n / 10);
    }
}
