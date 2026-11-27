import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UniqueHashtags {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String tags = Arrays.stream(scanner.nextLine().trim().split(" "))
                .distinct()
                .sorted()
                .map(t -> "#" + t)
                .collect(Collectors.joining(" "));
        System.out.println(tags);
    }
}
