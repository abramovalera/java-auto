import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NegativeRemoverFix {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> readings = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            readings.add(scanner.nextInt());
        }

        // Убираем сбойные показания датчика влажности: отрицательной влажности не бывает
        for (Integer value : readings) {
            if (value < 0) {
                readings.remove(value);
            }
        }

        System.out.println(readings);
    }
}
