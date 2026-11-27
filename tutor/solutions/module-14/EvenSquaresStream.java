import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EvenSquaresStream {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        List<Integer> squares = Arrays.stream(line.trim().split("\s+"))
                .map(Integer::parseInt)
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)
                .toList();

        System.out.println(squares);
    }
}
