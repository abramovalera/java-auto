import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarouselSeatsRotate {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<String> horses = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            horses.add(sc.next());
        }
        int k = sc.nextInt() % n;
        for (int i = 0; i < k; i++) {
            horses.add(0, horses.remove(horses.size() - 1));
        }
        System.out.println(String.join(" ", horses));
    }
}
