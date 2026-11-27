import java.util.Scanner;

public class BrokenAverage {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int first = scanner.nextInt();
        int second = scanner.nextInt();
        int third = scanner.nextInt();

        int sum = first + second + third;
        double average = sum / 3.0;
        double rounded = Math.round(average * 10) / 10.0;
        boolean passed = average >= 4.0;

        System.out.println("Сумма: " + sum);
        System.out.println("Средний балл: " + rounded);
        System.out.println("Зачёт: " + passed);
    }
}
