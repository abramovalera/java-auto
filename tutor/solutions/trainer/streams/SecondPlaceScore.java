import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class SecondPlaceScore {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String answer = Arrays.stream(scanner.nextLine().trim().split(" "))
                .map(Integer::parseInt)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst()
                .map(x -> "Второе место: " + x)
                .orElse("Второго места нет");
        System.out.println(answer);
    }
}
