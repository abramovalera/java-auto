import java.util.Scanner;

public class TicketHalvesSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String ticket = scanner.next();
        int left = (ticket.charAt(0) - '0') + (ticket.charAt(1) - '0') + (ticket.charAt(2) - '0');
        int right = (ticket.charAt(3) - '0') + (ticket.charAt(4) - '0') + (ticket.charAt(5) - '0');
        System.out.println("Слева: " + left + ", справа: " + right);
    }
}
