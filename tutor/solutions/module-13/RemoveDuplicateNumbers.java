import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

public class RemoveDuplicateNumbers {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            numbers.add(scanner.nextInt());
        }

        // LinkedHashSet выкидывает повторы и сохраняет порядок первого появления
        List<Integer> unique = new ArrayList<>(new LinkedHashSet<>(numbers));

        System.out.println(unique);
        System.out.println("Удалено повторов: " + (numbers.size() - unique.size()));
    }
}
