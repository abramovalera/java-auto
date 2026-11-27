import java.util.Scanner;

public class ArraySecondMax {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = scanner.nextInt();
        }

        int max = numbers[0];
        int second = 0;
        boolean hasSecond = false;           // флаг: нашлось ли значение меньше максимума
        for (int x : numbers) {
            if (x > max) {
                second = max;                // старый рекорд становится вторым
                hasSecond = true;
                max = x;
            } else if (x < max && (!hasSecond || x > second)) {
                second = x;
                hasSecond = true;
            }
        }

        if (hasSecond) {
            System.out.println("Второй по величине: " + second);
        } else {
            System.out.println("Второго нет");
        }
    }
}
