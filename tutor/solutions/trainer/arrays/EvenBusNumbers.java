import java.util.Scanner;

public class EvenBusNumbers {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] routes = new int[n];
        for (int i = 0; i < n; i++) {
            routes[i] = scanner.nextInt();
        }
        boolean found = false;
        for (int r : routes) {
            if (r % 2 == 0) {
                System.out.print(r + " ");
                found = true;
            }
        }
        if (found) {
            System.out.println();
        } else {
            System.out.println("Нет");
        }
    }
}
