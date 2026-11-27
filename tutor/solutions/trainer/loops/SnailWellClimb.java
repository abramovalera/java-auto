import java.util.Scanner;

public class SnailWellClimb {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int depth = scanner.nextInt();
        int up = scanner.nextInt();
        int down = scanner.nextInt();
        if (up < depth && up <= down) {
            System.out.println("Никогда");
        } else {
            int height = 0;
            int day = 0;
            while (true) {
                day++;
                height += up;
                if (height >= depth) {
                    break;
                }
                height -= down;
            }
            System.out.println(day);
        }
    }
}
