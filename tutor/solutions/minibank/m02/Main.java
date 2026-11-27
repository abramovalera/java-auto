
import java.util.Scanner;

/**
 * ★ Модуль 2 · «Карточка клиента».
 * Новое: переменные String и int, Scanner, склейка строк. Git: первая ветка и слияние.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("   Добро пожаловать в МиниБанк");
        System.out.println("==============================");
        System.out.println("Часы работы: круглосуточно");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Как вас зовут? ");
        String name = scanner.nextLine();
        int balance = 1000;

        System.out.println("Клиент: " + name);
        System.out.println("Баланс: " + balance + " ₽");
    }
}
