import java.util.Scanner;

public class ParcelDigitSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String number = scanner.nextLine().trim();
        int sum = number.chars()
                .map(c -> c - '0')
                .sum();
        System.out.println(sum);
    }
}
