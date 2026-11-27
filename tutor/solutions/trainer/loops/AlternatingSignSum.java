import java.util.Scanner;

public class AlternatingSignSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int position = 0;
        for (int step = 1; step <= n; step++) {
            if (step % 2 == 1) {
                position += step;
            } else {
                position -= step;
            }
        }
        System.out.println(position);
    }
}
