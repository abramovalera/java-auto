import java.util.InputMismatchException;
import java.util.Scanner;

public class StepGoalCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            int steps = scanner.nextInt();
            if (steps >= 10000) {
                System.out.println("Цель выполнена!");
            } else {
                System.out.println("Осталось пройти " + (10000 - steps) + " шагов");
            }
        } catch (InputMismatchException e) {
            System.out.println("Шаги считают целым числом");
        }
    }
}
