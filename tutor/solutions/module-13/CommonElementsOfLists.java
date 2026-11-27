import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CommonElementsOfLists {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> first = readList(scanner);
        List<Integer> second = readList(scanner);

        Set<Integer> common = new LinkedHashSet<>(first);   // порядок первого списка, без повторов
        common.retainAll(new HashSet<>(second));            // оставить только то, что есть во втором

        System.out.println(new ArrayList<>(common));
    }

    static List<Integer> readList(Scanner scanner) {
        int n = scanner.nextInt();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(scanner.nextInt());
        }
        return list;
    }
}
