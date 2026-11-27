import java.util.Scanner;

public class MiniGameLevels {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MiniLevel[] levels = new MiniLevel[n];
        for (int i = 0; i < n; i++) {
            String kind = scanner.next();
            if (kind.equals("forest")) {
                levels[i] = new MiniForestLevel();
            } else if (kind.equals("boss")) {
                levels[i] = new MiniBossLevel(scanner.nextInt());
            } else {
                levels[i] = new MiniCastleLevel();
            }
        }

        int passed = 0;
        for (int i = 0; i < levels.length; i++) {
            if (levels[i].start()) {
                passed++;
            }
        }
        System.out.println("Итого: " + passed + " из " + levels.length + " пройдено");
    }
}

class MiniLevel {
    private final String name;

    MiniLevel(String name) {
        this.name = name;
    }

    void enter() {
        System.out.println("  [вход] загружаю карту");
    }

    boolean play() {
        System.out.println("  уровень пустой");
        return false;
    }

    void leave() {
        System.out.println("  [выход] сохраняю игру");
    }

    final boolean start() {
        System.out.println("Уровень " + name + ":");
        enter();
        boolean ok = play();
        leave();
        System.out.println(ok ? "  ПРОЙДЕН" : "  ПРОВАЛЕН");
        return ok;
    }
}

class MiniForestLevel extends MiniLevel {
    MiniForestLevel() {
        super("Лес");
    }

    @Override
    boolean play() {
        System.out.println("  собираю грибы");
        return true;
    }
}

class MiniBossLevel extends MiniLevel {
    private final int power;

    MiniBossLevel(int power) {
        super("Босс");
        this.power = power;
    }

    @Override
    boolean play() {
        System.out.println("  бьюсь с боссом, сила: " + power);
        return power >= 50;
    }
}

class MiniCastleLevel extends MiniLevel {
    MiniCastleLevel() {
        super("Замок");
    }

    @Override
    void enter() {
        super.enter();
        System.out.println("  [вход] опускаю мост");
    }

    @Override
    boolean play() {
        System.out.println("  открываю сундук");
        return true;
    }
}
