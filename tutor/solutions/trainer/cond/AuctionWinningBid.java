import java.util.Scanner;

public class AuctionWinningBid {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int first = scanner.nextInt();
        int second = scanner.nextInt();
        int winner = first > second ? first : second;
        System.out.println(winner);
    }
}
