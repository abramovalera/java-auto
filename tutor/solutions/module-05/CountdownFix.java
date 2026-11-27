// Задача: обратный отсчёт перед стартом ракеты — 10, 9, …, 1 и «Пуск!».
public class CountdownFix {
    public static void main(String[] args) {
        int i = 10;
        while (i >= 1) {          // было i >= 0 — лишний круг с нулём
            System.out.println(i);
            i--;                  // было: шага нет — бесконечный цикл
        }
        System.out.println("Пуск!");
    }
}
