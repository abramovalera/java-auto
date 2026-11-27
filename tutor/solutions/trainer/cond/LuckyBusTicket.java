import java.util.Scanner;

public class LuckyBusTicket {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int left = n / 1000;
        int right = n % 1000;
        int leftSum = left / 100 + left / 10 % 10 + left % 10;
        int rightSum = right / 100 + right / 10 % 10 + right % 10;
        if (leftSum == rightSum) {
            System.out.println("Счастливый");
        } else {
            System.out.println("Обычный");
        }
    }
}
