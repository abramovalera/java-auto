import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class LongestWordPicker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String longest = Arrays.stream(scanner.nextLine().trim().split(" "))
                .max(Comparator.comparingInt(String::length))
                .get();
        System.out.println(longest + " (" + longest.length() + ")");
    }
}
