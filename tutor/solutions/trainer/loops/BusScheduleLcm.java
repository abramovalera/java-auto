import java.util.Scanner;

public class BusScheduleLcm {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long first = scanner.nextLong();
        long second = scanner.nextLong();
        long a = first;
        long b = second;
        while (b != 0) {
            long r = a % b;
            a = b;
            b = r;
        }
        System.out.println(first / a * second);
    }
}
