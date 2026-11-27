import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GuestListAlphabet {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String list = Arrays.stream(scanner.nextLine().trim().split(" "))
                .sorted()
                .collect(Collectors.joining(", "));
        System.out.println(list);
    }
}
