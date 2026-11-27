import java.util.Scanner;

public class GradeInputLoop {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int grade;
        int attempts = 0;                       // счётчик попыток
        do {
            grade = scanner.nextInt();          // спрашиваем хотя бы один раз
            attempts++;
            if (grade < 1 || grade > 5) {
                System.out.println("Нет такой оценки");
            }
        } while (grade < 1 || grade > 5);       // переспрашиваем, пока оценка неверная
        System.out.println("Оценка: " + grade + ", попыток: " + attempts);
    }
}
