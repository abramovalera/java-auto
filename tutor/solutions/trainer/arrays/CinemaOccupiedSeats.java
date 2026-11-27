import java.util.Scanner;

public class CinemaOccupiedSeats {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int r = scanner.nextInt();
        int c = scanner.nextInt();
        int[][] hall = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                hall[i][j] = scanner.nextInt();
            }
        }
        int taken = 0;
        for (int i = 0; i < hall.length; i++) {
            for (int j = 0; j < hall[i].length; j++) {
                if (hall[i][j] == 1) {
                    taken++;
                }
            }
        }
        System.out.println("Занято: " + taken + " из " + (r * c));
    }
}
