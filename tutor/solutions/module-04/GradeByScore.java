import java.util.Scanner;

public class GradeByScore {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int score = scanner.nextInt();
        // сначала отсекаем некорректный ввод, потом от узкого диапазона к широкому
        if (score < 0 || score > 100) {
            System.out.println("Ошибка: баллы от 0 до 100");
        } else if (score >= 90) {
            System.out.println("Оценка: A");
        } else if (score >= 80) {
            System.out.println("Оценка: B");
        } else if (score >= 70) {
            System.out.println("Оценка: C");
        } else if (score >= 60) {
            System.out.println("Оценка: D");
        } else {
            System.out.println("Оценка: F");
        }
    }
}
