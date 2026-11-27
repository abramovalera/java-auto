import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class CatalogPriceSort {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        CatalogProduct[] products = new CatalogProduct[n];
        for (int i = 0; i < n; i++) {
            products[i] = new CatalogProduct(scanner.next(), scanner.nextInt());
        }
        Arrays.sort(products, new ByCatalogPrice());
        for (int i = 0; i < n; i++) {
            System.out.println((i + 1) + ". " + products[i].name() + " — " + products[i].price() + " руб.");
        }
    }
}

record CatalogProduct(String name, int price) {
}

class ByCatalogPrice implements Comparator<CatalogProduct> {
    @Override
    public int compare(CatalogProduct a, CatalogProduct b) {
        return Integer.compare(a.price(), b.price());
    }
}
