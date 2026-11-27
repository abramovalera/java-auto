import java.util.Scanner;

public class PrinterJobFinally {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            int copies = Integer.parseInt(line);
            System.out.println("Печатаю копий: " + copies);
        } catch (NumberFormatException e) {
            System.out.println("Неверное количество копий");
        } finally {
            System.out.println("Принтер свободен");
        }
    }
}
