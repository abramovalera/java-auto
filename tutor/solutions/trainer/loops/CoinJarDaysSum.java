import java.util.Scanner;

public class CoinJarDaysSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        long sum = 0;
        for (int day = a; day <= b; day++) {
            sum += day;
        }
        System.out.println(sum);
    }
}
