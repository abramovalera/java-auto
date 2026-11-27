import java.util.Arrays;
import java.util.Scanner;

public class ReverseTrainCars {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] cars = new int[n];
        for (int i = 0; i < n; i++) {
            cars[i] = scanner.nextInt();
        }
        for (int i = 0; i < n / 2; i++) {
            int tmp = cars[i];
            cars[i] = cars[n - 1 - i];
            cars[n - 1 - i] = tmp;
        }
        System.out.println(Arrays.toString(cars));
    }
}
