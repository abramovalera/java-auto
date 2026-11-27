import java.util.Scanner;

public class FileNameFromTitle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String title = scanner.nextLine();
        String fileName = title.strip().toLowerCase().replace(" ", "_") + ".txt";
        System.out.println(fileName);
    }
}
