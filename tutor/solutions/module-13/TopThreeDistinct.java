import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class TopThreeDistinct {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Set<Integer> distinct = new TreeSet<>();          // без повторов и по возрастанию
        for (int i = 0; i < n; i++) {
            distinct.add(scanner.nextInt());
        }

        List<Integer> sorted = new ArrayList<>(distinct);
        Collections.sort(sorted, Collections.reverseOrder());   // по убыванию

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sorted.size() && i < 3; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(sorted.get(i));
        }
        System.out.println(result);
    }
}
