import java.util.Scanner;

public class GroceryItemRecord {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GroceryItem item = new GroceryItem(scanner.next(), scanner.nextInt(), scanner.nextInt());
        System.out.println(item);
        System.out.println("Товар: " + item.name());
        System.out.println("Итого: " + item.total() + " руб.");
    }
}

record GroceryItem(String name, int price, int qty) {
    int total() {
        return price * qty;
    }
}
