import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

record PricedGood(String title, int price) { }

public class PriciestThreeGoods {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Arrays.stream(scanner.nextLine().trim().split(" "))
                .map(s -> s.split(":"))
                .map(p -> new PricedGood(p[0], Integer.parseInt(p[1])))
                .sorted(Comparator.comparingInt(PricedGood::price).reversed()
                        .thenComparing(PricedGood::title))
                .limit(3)
                .forEach(g -> System.out.println(g.title() + " — " + g.price()));
    }
}
