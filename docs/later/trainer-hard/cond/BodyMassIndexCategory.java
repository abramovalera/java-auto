import java.util.Scanner;

public class BodyMassIndexCategory {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double weight = scanner.nextDouble();
        double height = scanner.nextDouble();
        double bmi = weight / (height * height);
        bmi = Math.round(bmi * 10) / 10.0;
        System.out.println("ИМТ: " + bmi);
        if (bmi < 18.5) {
            System.out.println("Недостаток веса");
        } else if (bmi < 25.0) {
            System.out.println("Норма");
        } else if (bmi < 30.0) {
            System.out.println("Избыточный вес");
        } else {
            System.out.println("Ожирение");
        }
    }
}
