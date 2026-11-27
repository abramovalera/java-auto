import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MostFrequentWord {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        Map<String, Long> freq = Arrays.stream(line.split(" "))
                .filter(w -> !w.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(w -> w, TreeMap::new, Collectors.counting()));
        String answer = freq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey() + ": " + e.getValue())
                .orElse("слов нет");
        System.out.println(answer);
    }
}
