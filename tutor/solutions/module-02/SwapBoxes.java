// Эталон задачи m02-t04. Обмен через временную переменную.
import java.util.Scanner;

public class SwapBoxes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введи a:");
        int a = scanner.nextInt();
        System.out.println("Введи b:");
        int b = scanner.nextInt();

        System.out.println("До: a = " + a + ", b = " + b);

        int temp = a;
        a = b;
        b = temp;

        System.out.println("После: a = " + a + ", b = " + b);
    }
}
