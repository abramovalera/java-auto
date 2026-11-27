import java.util.Scanner;

public class TicTacToeJudge {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char[][] field = new char[3][];
        for (int i = 0; i < 3; i++) {
            field[i] = scanner.next().toCharArray();
        }
        char winner = '.';
        for (int i = 0; i < 3; i++) {
            if (field[i][0] != '.' && field[i][0] == field[i][1] && field[i][1] == field[i][2]) {
                winner = field[i][0];
            }
            if (field[0][i] != '.' && field[0][i] == field[1][i] && field[1][i] == field[2][i]) {
                winner = field[0][i];
            }
        }
        if (field[1][1] != '.') {
            if (field[0][0] == field[1][1] && field[1][1] == field[2][2]) {
                winner = field[1][1];
            }
            if (field[0][2] == field[1][1] && field[1][1] == field[2][0]) {
                winner = field[1][1];
            }
        }
        boolean hasEmpty = false;
        for (char[] row : field) {
            for (char cell : row) {
                if (cell == '.') {
                    hasEmpty = true;
                }
            }
        }
        if (winner != '.') {
            System.out.println("Победил " + winner);
        } else if (hasEmpty) {
            System.out.println("Игра идёт");
        } else {
            System.out.println("Ничья");
        }
    }
}
