import java.util.Scanner;
import java.util.TreeSet;

public class BusRoutesAtStop {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        TreeSet<Integer> routes = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            routes.add(sc.nextInt());
        }
        System.out.println("Маршрутов: " + routes.size());
        System.out.println("Наименьший: " + routes.first());
        System.out.println("Наибольший: " + routes.last());
    }
}
