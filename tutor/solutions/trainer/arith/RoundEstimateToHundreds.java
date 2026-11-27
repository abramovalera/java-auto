import java.util.Scanner;

public class RoundEstimateToHundreds {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sum = scanner.nextInt();
        long rounded = Math.round(sum / 100.0) * 100;
        System.out.println("Около " + rounded + " руб.");
    }
}
