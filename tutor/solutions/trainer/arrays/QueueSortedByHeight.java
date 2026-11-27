import java.util.Scanner;

public class QueueSortedByHeight {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] heights = new int[n];
        for (int i = 0; i < n; i++) {
            heights[i] = scanner.nextInt();
        }
        int broken = -1;
        for (int i = 1; i < n; i++) {
            if (heights[i] < heights[i - 1]) {
                broken = i;
                break;
            }
        }
        if (broken == -1) {
            System.out.println("Порядок");
        } else {
            System.out.println("Нарушение: место " + (broken + 1));
        }
    }
}
