import java.util.Arrays;
import java.util.Scanner;

public class HighJumpRecord {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int best = Arrays.stream(scanner.nextLine().trim().split(" "))
                .mapToInt(Integer::parseInt)
                .max()
                .getAsInt();
        System.out.println("Рекорд: " + best + " см");
    }
}
