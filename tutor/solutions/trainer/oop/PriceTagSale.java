import java.util.Scanner;

public class PriceTagSale {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        int price = scanner.nextInt();
        int first = scanner.nextInt();
        int second = scanner.nextInt();
        PriceTag tag = new PriceTag(name, price);
        System.out.println(tag);
        tag.discount(first);
        System.out.println(tag);
        tag.discount(second);
        System.out.println(tag);
    }
}

class PriceTag {
    String name;
    int price;

    PriceTag(String name, int price) {
        this.name = name;
        this.price = price;
    }

    void discount(int percent) {
        price = price * (100 - percent) / 100;
    }

    @Override
    public String toString() {
        return name + ": " + price + " руб.";
    }
}
