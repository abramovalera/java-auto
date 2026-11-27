import java.util.Scanner;

public class RollerCoasterAccess {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int height = scanner.nextInt();
        int age = scanner.nextInt();
        String adult = scanner.next();
        if (height < 120) {
            System.out.println("Нельзя: рост меньше 120 см");
        } else if (age < 10 && !adult.equals("да")) {
            System.out.println("Нельзя: нужен взрослый");
        } else {
            System.out.println("Проходи!");
        }
    }
}
