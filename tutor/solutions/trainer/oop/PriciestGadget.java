import java.util.Scanner;

public class PriciestGadget {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        StoreGadget[] gadgets = new StoreGadget[n];
        for (int i = 0; i < n; i++) {
            gadgets[i] = new StoreGadget(scanner.next(), scanner.nextInt());
        }
        StoreGadget max = gadgets[0];
        StoreGadget min = gadgets[0];
        for (StoreGadget g : gadgets) {
            if (g.getPrice() > max.getPrice()) {
                max = g;
            }
            if (g.getPrice() < min.getPrice()) {
                min = g;
            }
        }
        System.out.println("Самый дорогой: " + max);
        System.out.println("Самый дешёвый: " + min);
    }
}

class StoreGadget {
    private final String name;
    private final int price;

    StoreGadget(String name, int price) {
        this.name = name;
        this.price = price;
    }

    String getName() {
        return name;
    }

    int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " за " + price + " руб.";
    }
}
