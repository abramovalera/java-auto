import java.util.Scanner;

public class AverageOfTwoGrades {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int first = scanner.nextInt();
        int second = scanner.nextInt();
        double average = (first + second) / 2.0;
        System.out.println("Средний балл: " + average);
    }
}
