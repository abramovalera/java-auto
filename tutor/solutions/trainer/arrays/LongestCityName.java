import java.util.Scanner;

public class LongestCityName {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[] cities = new String[n];
        for (int i = 0; i < n; i++) {
            cities[i] = scanner.next();
        }
        String longest = cities[0];
        for (String city : cities) {
            if (city.length() > longest.length()) {
                longest = city;
            }
        }
        System.out.println(longest);
    }
}
