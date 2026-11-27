import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LongWordsShout {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String result = Arrays.stream(scanner.nextLine().trim().split(" "))
                .filter(w -> w.length() > 3)
                .map(String::toUpperCase)
                .collect(Collectors.joining(" "));
        System.out.println(result);
    }
}
