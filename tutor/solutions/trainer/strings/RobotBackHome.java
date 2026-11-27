import java.util.Scanner;

public class RobotBackHome {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String moves = scanner.nextLine();
        int x = 0;
        int y = 0;
        for (int i = 0; i < moves.length(); i++) {
            switch (moves.charAt(i)) {
                case 'U' -> y++;
                case 'D' -> y--;
                case 'L' -> x--;
                case 'R' -> x++;
                default -> { }
            }
        }
        if (x == 0 && y == 0) {
            System.out.println("дома");
        } else {
            System.out.println("координаты: " + x + " " + y);
        }
    }
}
