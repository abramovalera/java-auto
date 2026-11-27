import java.util.Arrays;
import java.util.Scanner;

public class UniqueVisitors {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long unique = Arrays.stream(scanner.nextLine().trim().split(" "))
                .distinct()
                .count();
        System.out.println("Уникальных посетителей: " + unique);
    }
}
