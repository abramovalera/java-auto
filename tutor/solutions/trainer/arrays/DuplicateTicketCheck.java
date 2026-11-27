import java.util.Scanner;

public class DuplicateTicketCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] tickets = new int[n];
        for (int i = 0; i < n; i++) {
            tickets[i] = scanner.nextInt();
        }
        boolean found = false;
        int repeated = 0;
        for (int j = 1; j < n && !found; j++) {
            for (int i = 0; i < j; i++) {
                if (tickets[i] == tickets[j]) {
                    found = true;
                    repeated = tickets[j];
                    break;
                }
            }
        }
        if (found) {
            System.out.println("Повтор: " + repeated);
        } else {
            System.out.println("Все билеты разные");
        }
    }
}
