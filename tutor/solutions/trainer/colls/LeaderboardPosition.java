import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LeaderboardPosition {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<String> rating = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            rating.add(sc.next());
        }
        int index = rating.indexOf(sc.next());
        if (index == -1) {
            System.out.println("Нет в рейтинге");
        } else {
            System.out.println("Место: " + (index + 1));
        }
    }
}
