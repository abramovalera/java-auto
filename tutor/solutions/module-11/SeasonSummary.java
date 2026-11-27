import java.util.Scanner;

public class SeasonSummary {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] counts = new int[MatchResult.values().length];
        int points = 0;
        for (int i = 0; i < n; i++) {
            in.next(); // соперник для подсчёта не нужен
            MatchResult result = MatchResult.valueOf(in.next().toUpperCase());
            counts[result.ordinal()]++;
            points += switch (result) {
                case WIN -> 3;
                case DRAW -> 1;
                case LOSS -> 0;
            };
        }
        for (MatchResult r : MatchResult.values()) {
            System.out.println(r + ": " + counts[r.ordinal()]);
        }
        System.out.println("Очки: " + points);
        if (n == 0) {
            System.out.println("Сезон не начался");
        } else if (counts[MatchResult.WIN.ordinal()] > counts[MatchResult.LOSS.ordinal()]) {
            System.out.println("Удачный сезон");
        } else {
            System.out.println("Есть над чем поработать");
        }
    }
}

enum MatchResult {
    WIN, DRAW, LOSS
}
