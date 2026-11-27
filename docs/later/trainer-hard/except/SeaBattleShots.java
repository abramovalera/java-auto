import java.util.Scanner;

public class SeaBattleShots {
    public static void main(String[] args) {
        String[] field = {
                ".X...",
                ".X..X",
                "....X",
                "XX...",
                "....."
        };
        int shipCells = 0;
        for (String row : field) {
            for (int i = 0; i < row.length(); i++) {
                if (row.charAt(i) == 'X') {
                    shipCells++;
                }
            }
        }
        boolean[][] shot = new boolean[5][5];
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        int hits = 0;
        for (int i = 0; i < n && hits < shipCells; i++) {
            String text = scanner.nextLine();
            try {
                if (text.length() != 2) {
                    throw new IllegalArgumentException("Выстрел пишут так: B3");
                }
                int row = Character.toUpperCase(text.charAt(0)) - 'A';
                int col = Integer.parseInt(text.substring(1)) - 1;
                char cell = field[row].charAt(col);
                if (shot[row][col]) {
                    throw new SeaBattleRepeatShotException("Сюда уже стреляли");
                }
                shot[row][col] = true;
                if (cell == 'X') {
                    hits++;
                    System.out.println("Ранил");
                } else {
                    System.out.println("Мимо");
                }
            } catch (NumberFormatException e) {
                System.out.println("После буквы нужна цифра");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Мимо поля");
            } catch (SeaBattleRepeatShotException e) {
                System.out.println(e.getMessage());
            }
        }
        if (hits == shipCells) {
            System.out.println("Все корабли потоплены!");
        }
        System.out.println("Попаданий: " + hits + " из " + shipCells);
    }
}

class SeaBattleRepeatShotException extends Exception {
    SeaBattleRepeatShotException(String message) {
        super(message);
    }
}
