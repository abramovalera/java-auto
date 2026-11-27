import java.util.Scanner;

public class QueueTicketMachine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            QueueTicket ticket = new QueueTicket(sc.next());
            System.out.println(ticket.label());
        }
        System.out.println("Всего выдано: " + QueueTicket.issued);
        System.out.println("Следующий номер: " + QueueTicket.nextNumber());
    }
}

class QueueTicket {
    static int issued = 0;

    private final int number;
    private final String name;

    QueueTicket(String name) {
        issued++;
        this.number = issued;
        this.name = name;
    }

    String label() {
        return "Талон №" + number + ": " + name;
    }

    static int nextNumber() {
        return issued + 1;
    }
}
