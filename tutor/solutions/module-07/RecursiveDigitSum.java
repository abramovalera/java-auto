import java.util.Scanner;

public class RecursiveDigitSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println("Сумма цифр: " + digitSum(n));
    }

    static int digitSum(int n) {
        if (n < 0) {
            return digitSum(-n);
        }
        if (n < 10) {
            return n;
        }
        return n % 10 + digitSum(n / 10);
    }
}
