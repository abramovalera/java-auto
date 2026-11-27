import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LongWordsJoiner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int minLength = Integer.parseInt(sc.nextLine().trim());
        String line = sc.nextLine();

        String result = Arrays.stream(line.trim().split("\s+"))
                .filter(w -> w.length() > minLength)
                .map(String::toUpperCase)
                .collect(Collectors.joining(", "));

        System.out.println(result.isEmpty() ? "Таких слов нет" : result);
    }
}
