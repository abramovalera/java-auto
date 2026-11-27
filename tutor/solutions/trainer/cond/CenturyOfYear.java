import java.util.Scanner;

public class CenturyOfYear {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        int century = year / 100;
        if (year % 100 != 0) {
            century++;
        }
        System.out.println("Век: " + century);
    }
}
