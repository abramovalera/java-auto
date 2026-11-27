import java.util.Scanner;

public class AlarmClockTime {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            String[] parts = line.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Нужен формат чч:мм");
            }
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                throw new IllegalArgumentException("Нет такого времени: " + line);
            }
            System.out.println("Минут от полуночи: " + (hours * 60 + minutes));
        } catch (NumberFormatException e) {
            System.out.println("Часы и минуты пишут цифрами");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
