import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class TagsTwoOrders {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<String> inOrder = new LinkedHashSet<>();
        Set<String> sorted = new TreeSet<>();
        while (true) {
            String word = scanner.next();
            if (word.equals("END")) {
                break;
            }
            inOrder.add(word);
            sorted.add(word);
        }
        System.out.println("По порядку: " + inOrder);
        System.out.println("По алфавиту: " + sorted);
    }
}
