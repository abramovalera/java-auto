import java.util.Scanner;

public class DigitCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int count = 0;                 // счётчик
        do {                           // do-while: у нуля тоже одна цифра
            count++;
            n = n / 10;                // n — шагающая: теряет по цифре
        } while (n != 0);              // != 0, а не > 0 — чтобы работали отрицательные
        System.out.println(count);
    }
}
