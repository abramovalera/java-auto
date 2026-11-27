import java.util.Scanner;

public class ArrayEvenCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = scanner.nextInt();
        }

        int even = 0;
        int odd = 0;
        for (int x : numbers) {
            if (x % 2 == 0) {
                even++;
            } else {
                odd++;              // не x % 2 == 1: у отрицательных нечётных остаток -1
            }
        }

        System.out.println("Чётных: " + even);
        System.out.println("Нечётных: " + odd);
    }
}
