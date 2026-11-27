import java.util.Scanner;

public class ShopCartTotal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        if (n == 0) {
            System.out.println("Корзина пуста");
            return;
        }
        CartProduct[] cart = new CartProduct[n];
        for (int i = 0; i < n; i++) {
            cart[i] = new CartProduct(sc.next(), sc.nextInt(), sc.nextInt());
        }

        int total = 0;
        CartProduct priciest = cart[0];
        for (CartProduct p : cart) {
            System.out.println(p.title() + ": " + p.price() + " x " + p.qty() + " = " + p.cost());
            total += p.cost();
            if (p.cost() > priciest.cost()) {
                priciest = p;
            }
        }
        System.out.println("Итого: " + total);
        System.out.println("Самая дорогая позиция: " + priciest);
    }
}

record CartProduct(String title, int price, int qty) {
    int cost() {
        return price * qty;
    }
}
