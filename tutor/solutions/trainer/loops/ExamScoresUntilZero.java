import java.util.Scanner;

public class ExamScoresUntilZero {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        int sum = 0;
        int best = 0;
        int score = scanner.nextInt();
        while (score != 0) {
            count++;
            sum += score;
            if (score > best) {
                best = score;
            }
            score = scanner.nextInt();
        }
        if (count == 0) {
            System.out.println("Нет работ");
        } else {
            System.out.println("Работ: " + count);
            System.out.println("Лучший: " + best);
            System.out.println("Средний: " + Math.round((double) sum / count * 10) / 10.0);
        }
    }
}
