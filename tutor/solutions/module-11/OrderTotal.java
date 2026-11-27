import java.util.Scanner;

public class OrderTotal {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        OrderItem[] items = new OrderItem[n];
        int sum = 0;
        int repeats = 0;
        for (int i = 0; i < n; i++) {
            items[i] = new OrderItem(in.next(), in.nextInt(), in.nextInt());
            System.out.println(items[i]);
            sum += items[i].total();
            for (int j = 0; j < i; j++) {
                if (items[j].equals(items[i])) {
                    repeats++;
                    break;
                }
            }
        }
        System.out.println("Сумма заказа: " + sum);
        System.out.println("Повторов: " + repeats);
    }
}

record OrderItem(String title, int price, int qty) {
    OrderItem {
        if (price < 0) {
            price = 0;
        }
        if (qty < 1) {
            qty = 1;
        }
    }

    int total() {
        return price * qty;
    }
}
