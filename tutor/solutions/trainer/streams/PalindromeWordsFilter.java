import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PalindromeWordsFilter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String result = Arrays.stream(scanner.nextLine().trim().split(" "))
                .filter(w -> w.equalsIgnoreCase(new StringBuilder(w).reverse().toString()))
                .collect(Collectors.joining(" "));
        System.out.println(result.isEmpty() ? "нет" : result);
    }
}
