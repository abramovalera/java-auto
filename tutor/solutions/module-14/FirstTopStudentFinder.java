import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class FirstTopStudentFinder {

    record GradedStudent(String name, int score) { }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int threshold = Integer.parseInt(sc.nextLine().trim());
        String line = sc.nextLine();

        Optional<GradedStudent> first = Arrays.stream(line.trim().split("\s+"))
                .map(s -> s.split(":"))
                .map(p -> new GradedStudent(p[0], Integer.parseInt(p[1])))
                .filter(s -> s.score() > threshold)
                .findFirst();

        String answer = first
                .map(s -> s.name() + " (" + s.score() + ")")
                .orElse("Не найден");
        System.out.println(answer);
    }
}
