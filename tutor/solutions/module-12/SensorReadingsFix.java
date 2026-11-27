import java.util.Arrays;
import java.util.Scanner;

public class SensorReadingsFix {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] sensors = new int[4];
        int count = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine().trim();
            try {
                String[] parts = line.split(" ");
                int index = Integer.parseInt(parts[0]);
                int value = Integer.parseInt(parts[1]);
                sensors[index] = value;
                System.out.println("Датчик " + index + ": " + value);
            } catch (NumberFormatException e) {
                System.out.println("Не число в строке «" + line + "»");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Нет такого датчика в строке «" + line + "»");
            } catch (RuntimeException e) {
                System.out.println("Непонятная ошибка: " + e.getMessage());
            }
        }
        System.out.println("Итог: " + Arrays.toString(sensors));
    }
}
