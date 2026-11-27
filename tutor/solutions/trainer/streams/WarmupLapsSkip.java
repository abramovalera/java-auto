import java.util.Arrays;
import java.util.Scanner;

public class WarmupLapsSkip {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] laps = scanner.nextLine().trim().split(" ");
        int k = scanner.nextInt();
        int total = Arrays.stream(laps)
                .mapToInt(Integer::parseInt)
                .skip(k)
                .sum();
        System.out.println("Зачётное время: " + total);
    }
}
