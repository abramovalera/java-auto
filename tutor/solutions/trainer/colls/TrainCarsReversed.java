import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TrainCarsReversed {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<String> cars = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cars.add(sc.next());
        }
        if (cars.isEmpty()) {
            System.out.println("Вагонов нет");
            return;
        }
        Collections.reverse(cars);
        System.out.println(String.join(" - ", cars));
    }
}
