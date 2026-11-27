import java.util.Scanner;

public class EvenOddMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        for (int i = 0; i < k; i++) {
            int n = scanner.nextInt();
            if (isEven(n)) {
                System.out.println(n + " чётное");
            } else {
                System.out.println(n + " нечётное");
            }
        }
    }

    static boolean isEven(int n) {
        return n % 2 == 0;
    }
}
