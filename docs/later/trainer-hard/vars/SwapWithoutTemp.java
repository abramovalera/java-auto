import java.util.Scanner;

public class SwapWithoutTemp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int first = scanner.nextInt();
        int second = scanner.nextInt();
        first = first + second;
        second = first - second;
        first = first - second;
        System.out.println("Первое: " + first + ", второе: " + second);
    }
}
