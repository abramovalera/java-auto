import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class LongestMovies {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Movie[] movies = new Movie[n];
        for (int i = 0; i < n; i++) {
            movies[i] = new Movie(in.next(), in.nextInt());
        }
        Arrays.sort(movies, new MovieComparator());
        for (int i = 0; i < n; i++) {
            System.out.println((i + 1) + ". " + movies[i].title + " - " + movies[i].minutes + " мин");
        }
    }
}

class Movie {
    final String title;
    final int minutes;

    Movie(String title, int minutes) {
        this.title = title;
        this.minutes = minutes;
    }
}

class MovieComparator implements Comparator<Movie> {
    @Override
    public int compare(Movie a, Movie b) {
        if (a.minutes != b.minutes) {
            return Integer.compare(b.minutes, a.minutes); // сначала самые длинные
        }
        return a.title.compareTo(b.title);                // при равенстве — по алфавиту
    }
}
