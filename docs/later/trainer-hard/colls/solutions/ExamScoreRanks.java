import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class ExamScoreRanks {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Integer> scores = new ArrayList<>();
        Set<Integer> distinct = new TreeSet<>(Collections.reverseOrder());
        for (int i = 0; i < n; i++) {
            int s = sc.nextInt();
            scores.add(s);
            distinct.add(s);
        }
        Map<Integer, Integer> place = new HashMap<>();
        int p = 1;
        for (int s : distinct) {
            place.put(s, p);
            p++;
        }
        StringBuilder sb = new StringBuilder();
        for (int s : scores) {
            sb.append(place.get(s)).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}
