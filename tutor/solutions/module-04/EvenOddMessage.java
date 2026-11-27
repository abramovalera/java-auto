import java.util.Scanner;

public class EvenOddMessage {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        // n % 2 == 0, а не n % 2 == 1: у отрицательных нечётных остаток равен -1
        if (n % 2 == 0) {
            System.out.println("Число " + n + " чётное");
        } else {
            System.out.println("Число " + n + " нечётное");
        }
    }
}
