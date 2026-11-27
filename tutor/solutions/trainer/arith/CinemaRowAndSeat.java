import java.util.Scanner;

public class CinemaRowAndSeat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int seatsInRow = scanner.nextInt();
        int ticket = scanner.nextInt();
        int row = (ticket - 1) / seatsInRow + 1;
        int seat = (ticket - 1) % seatsInRow + 1;
        System.out.println("Ряд " + row + ", место " + seat);
    }
}
