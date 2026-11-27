import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class TopThreeProducts {

    record TopProduct(String title, int price) { }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        Arrays.stream(line.trim().split("\s+"))
                .map(s -> s.split(":"))
                .map(p -> new TopProduct(p[0], Integer.parseInt(p[1])))
                .sorted(Comparator.comparingInt(TopProduct::price).reversed()
                        .thenComparing(TopProduct::title))
                .limit(3)
                .forEach(p -> System.out.println(p.title() + " - " + p.price()));
    }
}
