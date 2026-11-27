import java.util.Scanner;

public class SumToN {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int sum = 0;                       // накопитель
        for (int i = 1; i <= n; i++) {     // i — шагающая: 1, 2, …, n
            sum += i;
        }
        System.out.println(sum);
    }
}
