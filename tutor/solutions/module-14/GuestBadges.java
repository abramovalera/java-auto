import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class GuestBadges {

    @FunctionalInterface
    interface BadgeFormat {
        String format(String name);
    }

    static void printAll(List<String> names, BadgeFormat format) {
        names.forEach(name -> System.out.println(format.format(name)));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        List<String> guests = new ArrayList<>(List.of(line.trim().split("\\s+")));
        guests.removeIf(name -> name.isEmpty() || name.startsWith("-"));

        Comparator<String> byLength = Comparator.comparing(name -> name.length());
        guests.sort(byLength.thenComparing(name -> name));

        if (guests.isEmpty()) {
            System.out.println("Гостей нет");
        } else {
            printAll(guests, name -> "Гость: " + name);
        }
    }
}
