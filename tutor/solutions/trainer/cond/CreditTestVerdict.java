import java.util.Scanner;

public class CreditTestVerdict {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int points = scanner.nextInt();
        System.out.println("Баллы: " + points + " — " + (points >= 60 ? "зачёт" : "незачёт"));
    }
}
