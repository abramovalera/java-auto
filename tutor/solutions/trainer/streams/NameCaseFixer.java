import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class NameCaseFixer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String names = Arrays.stream(scanner.nextLine().trim().split(" "))
                .map(n -> n.substring(0, 1).toUpperCase() + n.substring(1).toLowerCase())
                .collect(Collectors.joining(", "));
        System.out.println(names);
    }
}
