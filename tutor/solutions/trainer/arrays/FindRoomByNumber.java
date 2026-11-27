import java.util.Scanner;

public class FindRoomByNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] rooms = new int[n];
        for (int i = 0; i < n; i++) {
            rooms[i] = scanner.nextInt();
        }
        int wanted = scanner.nextInt();
        int index = -1;
        for (int i = 0; i < n; i++) {
            if (rooms[i] == wanted) {
                index = i;
                break;
            }
        }
        System.out.println(index);
    }
}
