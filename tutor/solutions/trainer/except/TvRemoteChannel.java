import java.util.Scanner;

public class TvRemoteChannel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            int channel = Integer.parseInt(line);
            if (channel < 1 || channel > 99) {
                throw new IllegalArgumentException("Канала " + channel + " нет");
            }
            System.out.println("Включаю канал " + channel);
        } catch (NumberFormatException e) {
            System.out.println("Нажимай только цифры");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
