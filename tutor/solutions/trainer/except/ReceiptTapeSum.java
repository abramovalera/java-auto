import java.util.Scanner;

public class ReceiptTapeSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] parts = scanner.nextLine().trim().split("\\s+");
        long sum = 0;
        int skipped = 0;
        for (String part : parts) {
            try {
                sum += Integer.parseInt(part);
            } catch (NumberFormatException e) {
                skipped++;
            }
        }
        System.out.println("Сумма: " + sum);
        System.out.println("Пропущено: " + skipped);
    }
}
