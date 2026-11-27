import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class SensorNegativesCleanup {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Integer> readings = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            readings.add(sc.nextInt());
        }
        int removed = 0;
        Iterator<Integer> it = readings.iterator();
        while (it.hasNext()) {
            if (it.next() < 0) {
                it.remove();
                removed++;
            }
        }
        System.out.println("Удалено: " + removed);
        System.out.println(readings);
    }
}
