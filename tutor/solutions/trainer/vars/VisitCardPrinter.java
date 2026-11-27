import java.util.Scanner;

public class VisitCardPrinter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        String job = scanner.nextLine();
        String phone = scanner.nextLine();
        System.out.println("********************");
        System.out.println("Имя: " + name);
        System.out.println("Профессия: " + job);
        System.out.println("Телефон: " + phone);
        System.out.println("********************");
    }
}
