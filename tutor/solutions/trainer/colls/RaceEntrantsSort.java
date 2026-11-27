import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

record RaceEntrant(String name, int age) { }

class RaceEntrantOrder implements Comparator<RaceEntrant> {
    @Override
    public int compare(RaceEntrant a, RaceEntrant b) {
        int byAge = Integer.compare(a.age(), b.age());
        if (byAge != 0) {
            return byAge;
        }
        return a.name().compareTo(b.name());
    }
}

public class RaceEntrantsSort {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<RaceEntrant> entrants = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            int age = sc.nextInt();
            entrants.add(new RaceEntrant(name, age));
        }
        entrants.sort(new RaceEntrantOrder());
        for (RaceEntrant e : entrants) {
            System.out.println(e.name() + " (" + e.age() + ")");
        }
    }
}
