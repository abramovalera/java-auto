import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AllBasketItems {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        List<String> all = Arrays.stream(line.split(";"))
                .flatMap(basket -> Arrays.stream(basket.split(",")))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .toList();

        List<String> unique = all.stream()
                .distinct()
                .sorted()
                .toList();

        System.out.println("Всего покупок: " + all.size());
        System.out.println("Разные товары: " + unique);
    }
}
