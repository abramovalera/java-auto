import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SquaresInLine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String result = Arrays.stream(scanner.nextLine().trim().split(" "))
                .mapToInt(Integer::parseInt)
                .map(x -> x * x)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
        System.out.println(result);
    }
}
