import java.util.Scanner;

public class DistanceToZero {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        if (n < 0) {
            n = -n;
        }
        System.out.println(n);
    }
}
