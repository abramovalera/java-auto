import java.util.Scanner;

public class BinarySearchGuessSteps {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] books = new int[n];
        for (int i = 0; i < n; i++) {
            books[i] = scanner.nextInt();
        }
        int wanted = scanner.nextInt();
        int lo = 0;
        int hi = n - 1;
        int steps = 0;
        int index = -1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            steps++;
            if (books[mid] == wanted) {
                index = mid;
                break;
            } else if (books[mid] < wanted) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        if (index == -1) {
            System.out.println("Не найдено, шагов: " + steps);
        } else {
            System.out.println("Индекс " + index + ", шагов: " + steps);
        }
    }
}
