import java.util.Scanner;

public class BacteriaDoubling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long count = scanner.nextLong();
        int hours = scanner.nextInt();
        for (int h = 1; h <= hours; h++) {
            count *= 2;
        }
        System.out.println(count);
    }
}
