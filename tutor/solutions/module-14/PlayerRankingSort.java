import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class PlayerRankingSort {

    record RankedPlayer(String name, int score) { }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        Arrays.stream(line.trim().split("\s+"))
                .map(s -> s.split(":"))
                .map(p -> new RankedPlayer(p[0], Integer.parseInt(p[1])))
                .sorted(Comparator.comparingInt(RankedPlayer::score).reversed()
                        .thenComparing(RankedPlayer::name))
                .forEach(p -> System.out.println(p.name() + " " + p.score()));
    }
}
