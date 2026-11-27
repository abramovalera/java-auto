import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrimesUpToN {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String primes = IntStream.rangeClosed(2, n)
                .filter(x -> IntStream.rangeClosed(2, (int) Math.sqrt(x)).noneMatch(d -> x % d == 0))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
        System.out.println(primes.isEmpty() ? "нет" : primes);
    }
}
