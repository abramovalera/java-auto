import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookShelfCatalog {
    private final List<String> titles = new ArrayList<>();

    void add(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название книги не может быть пустым");
        }
        titles.add(title.strip());
    }

    int size() {
        return titles.size();
    }

    // все книги, в названии которых есть слово (без учёта регистра), в порядке добавления
    List<String> findByWord(String word) {
        List<String> found = new ArrayList<>();
        for (String t : titles) {
            if (t.toLowerCase().contains(word.toLowerCase())) {
                found.add(t);
            }
        }
        return found;
    }
}

public class BookShelfCatalogTest {
    private BookShelfCatalog catalog;

    @BeforeEach
    void setUp() {
        catalog = new BookShelfCatalog();
        catalog.add("Java для начинающих");
        catalog.add("Чистый код");
        catalog.add("Java. Эффективное программирование");
    }

    @Test
    void sizeCountsAddedBooks() {
        assertThat(catalog.size()).isEqualTo(3);
    }

    @Test
    void findsAllBooksWithWordInOrder() {
        assertThat(catalog.findByWord("Java"))
                .containsExactly("Java для начинающих", "Java. Эффективное программирование");
    }

    @Test
    void searchIgnoresCase() {
        assertThat(catalog.findByWord("КОД")).containsExactly("Чистый код");
    }

    @Test
    void unknownWordGivesEmptyList() {
        assertThat(catalog.findByWord("Python")).isEmpty();
    }

    @Test
    void titleIsStripped() {
        catalog.add("   Война и мир  ");
        assertThat(catalog.findByWord("война")).containsExactly("Война и мир");
        assertThat(catalog.size()).isEqualTo(4);
    }

    @Test
    void blankTitleIsRejected() {
        assertThatThrownBy(() -> catalog.add("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("пуст");
    }
}
