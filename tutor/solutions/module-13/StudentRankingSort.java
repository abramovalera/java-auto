import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class StudentRankingSort {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<RankedStudent> students = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String name = scanner.next();
            int score = scanner.nextInt();
            students.add(new RankedStudent(name, score));
        }

        students.sort(new RankedStudentComparator());

        for (int i = 0; i < students.size(); i++) {
            RankedStudent s = students.get(i);
            System.out.println((i + 1) + ". " + s.name() + " " + s.score());
        }
    }
}

record RankedStudent(String name, int score) { }

class RankedStudentComparator implements Comparator<RankedStudent> {
    @Override
    public int compare(RankedStudent a, RankedStudent b) {
        int byScore = Integer.compare(b.score(), a.score());   // по убыванию балла: b и a поменяны местами
        if (byScore != 0) {
            return byScore;
        }
        return a.name().compareTo(b.name());                    // при равенстве — по алфавиту
    }
}
