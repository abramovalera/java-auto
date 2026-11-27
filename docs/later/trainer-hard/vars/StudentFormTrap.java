import java.util.Scanner;

public class StudentFormTrap {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        int age = scanner.nextInt();
        scanner.nextLine();
        String city = scanner.nextLine();
        int height = scanner.nextInt();
        scanner.nextLine();
        String motto = scanner.nextLine();
        System.out.println("=== Анкета ===");
        System.out.println("Имя: " + name);
        System.out.println("Возраст: " + age + " (в следующем году — " + (age + 1) + ")");
        System.out.println("Город: " + city);
        System.out.println("Рост: " + height + " см");
        System.out.println("Девиз: " + motto);
        System.out.println("==============");
    }
}
