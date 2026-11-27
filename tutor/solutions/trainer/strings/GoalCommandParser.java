import java.util.Scanner;

public class GoalCommandParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String commands = scanner.nextLine();
        System.out.println(commands.replace("()", "o").replace("(al)", "al"));
    }
}
