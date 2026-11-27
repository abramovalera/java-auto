import java.util.Arrays;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.stream.IntStream;

public class LineupSortedCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] h = Arrays.stream(scanner.nextLine().trim().split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
        OptionalInt bad = IntStream.range(1, h.length)
                .filter(i -> h[i] < h[i - 1])
                .findFirst();
        System.out.println(bad.isPresent() ? "Сбой на месте " + (bad.getAsInt() + 1) : "Строй по росту");
    }
}
