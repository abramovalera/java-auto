// Эталон задачи m02-t07. Без оператора % (он в модуле 3).
import java.util.Scanner;

public class MinutesToHours {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Сколько минут?");
        int totalMinutes = scanner.nextInt();

        int hours = totalMinutes / 60;
        int minutes = totalMinutes - hours * 60;

        System.out.println(totalMinutes + " мин = " + hours + " ч " + minutes + " мин");
    }
}
