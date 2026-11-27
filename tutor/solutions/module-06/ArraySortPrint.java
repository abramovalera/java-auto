import java.util.Arrays;
import java.util.Scanner;

public class ArraySortPrint {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = scanner.nextInt();
        }

        System.out.println("Было: " + Arrays.toString(numbers));
        Arrays.sort(numbers);
        System.out.println("Стало: " + Arrays.toString(numbers));
        System.out.println("Мин: " + numbers[0] + ", макс: " + numbers[numbers.length - 1]);
    }
}
