import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ScoresHighToLow {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            scores.add(sc.nextInt());
        }
        Collections.sort(scores, Collections.reverseOrder());
        for (int s : scores) {
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
