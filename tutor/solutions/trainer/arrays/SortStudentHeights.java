import java.util.Arrays;
import java.util.Scanner;

public class SortStudentHeights {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] heights = new int[n];
        for (int i = 0; i < n; i++) {
            heights[i] = scanner.nextInt();
        }
        Arrays.sort(heights);
        System.out.println(Arrays.toString(heights));
    }
}
