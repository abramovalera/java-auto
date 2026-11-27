import java.util.Scanner;

public class DigitReverser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int result = 0;                    // накопитель: собирает число задом наперёд
        while (n != 0) {                   // != 0 — работает и для отрицательных
            int digit = n % 10;            // последняя цифра (у отрицательного — со знаком минус)
            result = result * 10 + digit;  // сдвигаем влево и ставим цифру справа
            n = n / 10;                    // отрываем последнюю цифру
        }
        System.out.println(result);
    }
}
