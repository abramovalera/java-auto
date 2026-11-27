import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class GradeFrequency {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<Integer, Long> freq = Arrays.stream(scanner.nextLine().trim().split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.groupingBy(g -> g, TreeMap::new, Collectors.counting()));
        freq.forEach((grade, count) -> System.out.println(grade + ": " + count));
    }
}
