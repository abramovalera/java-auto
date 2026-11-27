import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class KindergartenGroupRoster {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, List<String>> groups = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            String group = sc.next();
            if (!groups.containsKey(group)) {
                groups.put(group, new ArrayList<>());
            }
            groups.get(group).add(name);
        }
        for (Map.Entry<String, List<String>> e : groups.entrySet()) {
            System.out.println(e.getKey() + ": " + String.join(", ", e.getValue()));
        }
    }
}
