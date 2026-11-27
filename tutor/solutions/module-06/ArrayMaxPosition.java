import java.util.Scanner;

public class ArrayMaxPosition {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = scanner.nextInt();
        }

        int max = numbers[0];
        int maxIndex = 0;
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {     // строго больше — остаётся первый из равных
                max = numbers[i];
                maxIndex = i;
            }
        }

        System.out.println("Максимум: " + max);
        System.out.println("Индекс: " + maxIndex);
    }
}
