import java.util.Scanner;

public class EvenOddGuess {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        if (isEven(n)) {
            System.out.println(n + " — чёт");
        } else {
            System.out.println(n + " — нечет");
        }
    }

    static boolean isEven(int n) {
        return n % 2 == 0;
    }
}
