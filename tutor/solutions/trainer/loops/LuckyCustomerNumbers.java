import java.util.Scanner;

public class LuckyCustomerNumbers {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        if (k > n) {
            System.out.println("Нет подарков");
        } else {
            for (int i = k; i <= n; i += k) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }
}
