import java.util.Scanner;

public class GamePlayerCard {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        int ups = scanner.nextInt();
        GamePlayer player = new GamePlayer(name);
        System.out.println("Было: " + player);
        for (int i = 0; i < ups; i++) {
            player.levelUp();
        }
        System.out.println("Стало: " + player);
    }
}

class GamePlayer {
    String name;
    int level;

    GamePlayer(String name) {
        this.name = name;
        this.level = 1;
    }

    void levelUp() {
        if (level < 10) {
            level++;
        }
    }

    @Override
    public String toString() {
        return name + " (уровень " + level + ")";
    }
}
