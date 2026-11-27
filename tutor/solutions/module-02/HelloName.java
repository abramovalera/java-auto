// Эталон задачи m02-t02. Главное: nextLine(), а не next() — имя может быть из нескольких слов.
import java.util.Scanner;

public class HelloName {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Как тебя зовут?");
        String name = scanner.nextLine();
        System.out.println("Привет, " + name + "!");
    }
}
