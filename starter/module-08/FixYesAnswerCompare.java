// Задача 1: программа спрашивает «Продолжить?» и должна понимать ответ «да»
// в любом регистре и с лишними пробелами по краям: «да», «ДА», «  Да  ».
// На «да» печатает «Продолжаем!», на всё остальное — «Стоп.».
// Её писали в спешке: она компилируется, но «да» не понимает никогда.
// Найди и исправь ошибки — не переписывай всё заново.
import java.util.Scanner;

public class FixYesAnswerCompare {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();
        answer.trim();
        answer.toLowerCase();
        if (answer == "да") {
            System.out.println("Продолжаем!");
        } else {
            System.out.println("Стоп.");
        }
    }
}
