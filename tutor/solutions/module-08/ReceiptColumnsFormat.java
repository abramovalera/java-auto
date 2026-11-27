import java.util.Scanner;

public class ReceiptColumnsFormat {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int order = sc.nextInt();
        int n = sc.nextInt();
        System.out.println(String.format("Чек #%05d", order));
        int total = 0;
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            int qty = sc.nextInt();
            int price = sc.nextInt();
            int sum = qty * price;
            total += sum;
            System.out.printf("%-10s%4d%8d%n", name, qty, sum);
        }
        System.out.printf("%-14s%8d%n", "ИТОГО", total);
    }
}
