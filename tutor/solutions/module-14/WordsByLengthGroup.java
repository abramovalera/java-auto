import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class WordsByLengthGroup {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        Map<Integer, List<String>> byLength = Arrays.stream(line.trim().split("\s+"))
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList()));

        byLength.forEach((length, words) -> System.out.println(length + ": " + words));
    }
}
