import java.util.Scanner;

public class MountainEcho {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.next();
        System.out.println("Ты крикнул: " + word);
        System.out.println("Горы ответили: " + word + "... " + word + "... " + word + "...");
    }
}
