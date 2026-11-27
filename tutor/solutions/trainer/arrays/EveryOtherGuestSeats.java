import java.util.Scanner;

public class EveryOtherGuestSeats {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] guests = new int[n];
        for (int i = 0; i < n; i++) {
            guests[i] = scanner.nextInt();
        }
        for (int i = 0; i < n; i += 2) {
            System.out.print(guests[i] + " ");
        }
        System.out.println();
    }
}
