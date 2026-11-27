import java.util.Scanner;

public class FootballScoreBoard {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FootballMatch match = new FootballMatch(scanner.next(), scanner.next());
        String goals = scanner.next();
        for (int i = 0; i < goals.length(); i++) {
            char c = goals.charAt(i);
            if (c == 'H') {
                match.homeScored();
            } else if (c == 'A') {
                match.awayScored();
            }
        }
        System.out.println(match.score());
        System.out.println(match.winner());
    }
}

class FootballMatch {
    String home;
    String away;
    int homeGoals;
    int awayGoals;

    FootballMatch(String home, String away) {
        this.home = home;
        this.away = away;
    }

    void homeScored() {
        homeGoals++;
    }

    void awayScored() {
        awayGoals++;
    }

    String score() {
        return home + " " + homeGoals + ":" + awayGoals + " " + away;
    }

    String winner() {
        if (homeGoals > awayGoals) {
            return "Победил " + home;
        }
        if (awayGoals > homeGoals) {
            return "Победил " + away;
        }
        return "Ничья";
    }
}
