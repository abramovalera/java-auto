import java.util.Arrays;
import java.util.Scanner;

public class ExamAllPassed {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean all = Arrays.stream(scanner.nextLine().trim().split(" "))
                .mapToInt(Integer::parseInt)
                .allMatch(score -> score >= 50);
        System.out.println(all);
    }
}
