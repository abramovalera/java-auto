import java.util.Scanner;

public class ElevatorLoadLimit {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int capacity = scanner.nextInt();
        int n = scanner.nextInt();
        int entered = 0;
        int load = 0;
        for (int i = 0; i < n; i++) {
            int weight = scanner.nextInt();
            if (load + weight > capacity) {
                break;
            }
            load += weight;
            entered++;
        }
        System.out.println("Вошли: " + entered + ", вес: " + load);
    }
}
