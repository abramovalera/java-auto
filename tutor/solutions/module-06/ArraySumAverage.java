import java.util.Scanner;

public class ArraySumAverage {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = scanner.nextInt();
        }

        int sum = 0;
        for (int x : numbers) {
            sum += x;
        }
        double average = (double) sum / numbers.length;

        int aboveAverage = 0;
        for (int x : numbers) {
            if (x > average) {
                aboveAverage++;
            }
        }

        System.out.println("Сумма: " + sum);
        System.out.println("Среднее: " + average);
        System.out.println("Выше среднего: " + aboveAverage);
    }
}
