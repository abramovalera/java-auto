import java.util.Arrays;
import java.util.Scanner;

public class ArrayReverseInPlace {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = scanner.nextInt();
        }

        int left = 0;
        int right = numbers.length - 1;
        while (left < right) {
            int tmp = numbers[left];
            numbers[left] = numbers[right];
            numbers[right] = tmp;
            left++;
            right--;
        }

        System.out.println(Arrays.toString(numbers));
    }
}
