import java.util.Scanner;

public class CelsiusFahrenheitTable {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int from = scanner.nextInt();
        int to = scanner.nextInt();
        for (int c = from; c <= to; c += 10) {
            int f = c * 9 / 5 + 32;
            System.out.println(c + " C = " + f + " F");
        }
    }
}
