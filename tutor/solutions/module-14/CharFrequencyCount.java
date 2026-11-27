import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CharFrequencyCount {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        Map<String, Long> frequency = Arrays.stream(line.toLowerCase().split(""))
                .filter(ch -> !ch.isBlank())
                .collect(Collectors.groupingBy(ch -> ch, TreeMap::new, Collectors.counting()));

        frequency.forEach((ch, count) -> System.out.println(ch + ": " + count));
    }
}
