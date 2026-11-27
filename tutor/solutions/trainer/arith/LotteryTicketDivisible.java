import java.util.Scanner;

public class LotteryTicketDivisible {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int ticket = scanner.nextInt();
        boolean prize = ticket % 3 == 0 && ticket % 5 == 0;
        System.out.println(prize);
    }
}
