import java.util.Scanner;

// Программа читает три оценки и считает средний балл.
// Зачёт ставится, если средний балл не меньше 4.0.
// Её писали в спешке — найди и исправь ошибки, не переписывая всё заново.
public class BrokenAverage {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int first = scanner.nextInt();
        int second = scanner.nextInt();
        int third = scanner.nextInt();

        double average = first + second + third / 3;
        double rounded = Math.round(average * 10) / 10;
        boolean passed = average => 4.0;

        System.out.println("Сумма: " + first + second + third);
        System.out.println("Средний балл: " + rounded);
        System.out.println("Зачёт: " + passed);
    }
}
