import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PodiumTopThree {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String top = Arrays.stream(scanner.nextLine().trim().split(" "))
                .map(Integer::parseInt)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        System.out.println(top);
    }
}
