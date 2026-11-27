import java.util.Arrays;
import java.util.Scanner;

record CartLineItem(String title, int price, int qty) { }

public class ShoppingCartTotal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int total = Arrays.stream(scanner.nextLine().trim().split(" "))
                .map(s -> s.split(":"))
                .map(p -> new CartLineItem(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2])))
                .mapToInt(item -> item.price() * item.qty())
                .sum();
        System.out.println("Итого: " + total);
    }
}
