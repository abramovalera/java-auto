import java.util.Scanner;

// m18-t06 — эталон. Имитация «умного ожидания» Selenide: опрашиваем, пока не появится или не выйдет время.
// Время виртуальное (переменная t), настоящего ожидания нет.
public class NookSmartWait {

    /** Возвращает момент, когда элемент найден, или -1, если время вышло. Печатает каждую проверку. */
    static int waitUntilVisible(int appearAt, int timeout, int poll) {
        int t = 0;
        int checks = 0;
        while (true) {
            checks++;
            boolean visible = appearAt >= 0 && t >= appearAt;
            System.out.println(t + " мс: " + (visible ? "виден" : "не виден"));
            if (visible) {
                System.out.println("Готово за " + t + " мс, проверок: " + checks);
                return t;
            }
            if (t >= timeout) {
                System.out.println("Время вышло: элемент не появился за " + timeout + " мс, проверок: " + checks);
                return -1;
            }
            t = Math.min(t + poll, timeout);
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int appearAt = in.nextInt();
        int timeout = in.nextInt();
        int poll = in.nextInt();
        if (poll <= 0) {
            System.out.println("Ошибка: шаг опроса должен быть больше 0");
            return;
        }
        int found = waitUntilVisible(appearAt, timeout, poll);
        if (found >= 0) {
            System.out.println("Thread.sleep(" + timeout + ") ждал бы на " + (timeout - found) + " мс дольше");
        } else {
            System.out.println("Тест упал: элемент так и не появился");
        }
    }
}
