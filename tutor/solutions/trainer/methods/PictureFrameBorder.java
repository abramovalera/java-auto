import java.util.Scanner;

public class PictureFrameBorder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        char c = scanner.next().charAt(0);
        printFrame(width, height, c);
    }

    static void printFrame(int width, int height, char c) {
        for (int row = 0; row < height; row++) {
            String line = "";
            for (int col = 0; col < width; col++) {
                boolean edge = row == 0 || row == height - 1 || col == 0 || col == width - 1;
                if (edge) {
                    line = line + c;
                } else {
                    line = line + " ";
                }
            }
            System.out.println(line);
        }
    }
}
