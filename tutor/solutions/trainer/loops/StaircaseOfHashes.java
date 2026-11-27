import java.util.Scanner;

public class StaircaseOfHashes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int row = 1; row <= n; row++) {
            for (int i = 1; i <= row; i++) {
                System.out.print("#");
            }
            System.out.println();
        }
    }
}
