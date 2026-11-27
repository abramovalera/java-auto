
import java.util.Scanner;

/**
 * ★ Модуль 3 · «Пополнение с комиссией».
 * Новое: арифметика, Math.round, приведение типа, boolean-выражение (ещё без if).
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

        System.out.print("Сумма пополнения: ");
        int deposit = scanner.nextInt();
        int commission = (int) Math.round(deposit * 0.02);
        int credited = deposit - commission;
        balance = balance + credited;

        System.out.println("Комиссия: " + commission + " ₽");
        System.out.println("Зачислено: " + credited + " ₽");
        System.out.println("Новый баланс: " + balance + " ₽");
        System.out.println("Хватит на перевод 1500 ₽: " + (balance >= 1500));
    }
}
