import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DiceRollFrequency {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Integer> rolls = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            rolls.add(sc.nextInt());
        }
        int k = sc.nextInt();
        System.out.println("Сколько раз выпало " + k + ": " + Collections.frequency(rolls, k));
    }
}
