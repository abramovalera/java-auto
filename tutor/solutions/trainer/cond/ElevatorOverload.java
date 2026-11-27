import java.util.Scanner;

public class ElevatorOverload {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int weight = scanner.nextInt();
        if (weight <= 400) {
            System.out.println("Поехали!");
        } else {
            System.out.println("Перегруз на " + (weight - 400) + " кг");
        }
    }
}
