import java.util.Scanner;

public class CandySharingDivisors {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int kids = 1; kids <= n; kids++) {
            if (n % kids == 0) {
                System.out.print(kids + " ");
            }
        }
        System.out.println();
    }
}
