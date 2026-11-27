import java.util.Scanner;

public class BusSeatSide {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int seat = scanner.nextInt();
        if (seat % 2 == 0) {
            System.out.println("У прохода");
        } else {
            System.out.println("У окна");
        }
    }
}
