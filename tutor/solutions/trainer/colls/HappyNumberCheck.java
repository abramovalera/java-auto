import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class HappyNumberCheck {
    static int digitSquares(int n) {
        int sum = 0;
        while (n > 0) {
            int d = n % 10;
            sum += d * d;
            n /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<Integer> seen = new HashSet<>();
        StringBuilder chain = new StringBuilder();
        chain.append(n);
        seen.add(n);
        while (n != 1) {
            n = digitSquares(n);
            chain.append(" -> ").append(n);
            if (!seen.add(n)) {
                break;
            }
        }
        System.out.println(chain);
        System.out.println(n == 1 ? "Счастливое" : "Несчастливое");
    }
}
