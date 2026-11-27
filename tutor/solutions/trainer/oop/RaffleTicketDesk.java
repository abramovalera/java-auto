import java.util.Scanner;

public class RaffleTicketDesk {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            RaffleTicket ticket = new RaffleTicket(scanner.next());
            System.out.println(ticket);
        }
        System.out.println("Всего билетов: " + RaffleTicket.getIssued());
    }
}

class RaffleTicket {
    private static int issued = 0;

    private final int number;
    private final String owner;

    RaffleTicket(String owner) {
        issued++;
        this.number = issued;
        this.owner = owner;
    }

    static int getIssued() {
        return issued;
    }

    @Override
    public String toString() {
        return "Билет №" + number + " — " + owner;
    }
}
