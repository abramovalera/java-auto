import java.util.Scanner;

public class TicketPrinterMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String passenger = scanner.nextLine();
        int seat = scanner.nextInt();
        printTicket(passenger, seat);
    }

    static void printTicket(String passenger, int seat) {
        System.out.println("=== БИЛЕТ ===");
        System.out.println("Пассажир: " + passenger);
        System.out.println("Место: " + seat);
    }
}
