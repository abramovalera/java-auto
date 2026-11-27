import java.util.Scanner;

public class PiggyBankSumUpTo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println("Через " + n + " дн. в копилке: " + sumUpTo(n) + " ₽");
    }

    static long sumUpTo(int n) {
        long sum = 0;
        for (int day = 1; day <= n; day++) {
            sum += day;
        }
        return sum;
    }
}
