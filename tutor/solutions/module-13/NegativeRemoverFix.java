import java.util.ArrayList;
import java.util.Iterator;
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
        Iterator<Integer> it = readings.iterator();
        while (it.hasNext()) {
            Integer value = it.next();
            if (value < 0) {
                it.remove();          // удаляем через итератор — он не собьётся
            }
        }

        System.out.println(readings);
    }
}
