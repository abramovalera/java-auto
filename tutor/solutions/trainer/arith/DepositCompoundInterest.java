import java.util.Scanner;

public class DepositCompoundInterest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sum = scanner.nextInt();
        int rate = scanner.nextInt();
        int years = scanner.nextInt();
        double result = sum * Math.pow(1 + rate / 100.0, years);
        System.out.println("На счёте: " + Math.round(result * 100) / 100.0);
    }
}
