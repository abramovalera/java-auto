import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CinemaTicketPrice {
    // цена билета в кино по возрасту
    static int priceForAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Возраст не может быть отрицательным: " + age);
        }
        if (age < 7) {
            return 0;
        }
        if (age < 18) {
            return 250;
        }
        if (age < 65) {
            return 400;
        }
        return 200;
    }
}

public class CinemaTicketPriceTest {

    @ParameterizedTest(name = "возраст {0} → {1} ₽")
    @CsvSource({
            "0, 0",
            "6, 0",
            "7, 250",
            "17, 250",
            "18, 400",
            "64, 400",
            "65, 200",
            "90, 200"
    })
    void priceDependsOnAge(int age, int expectedPrice) {
        assertEquals(expectedPrice, CinemaTicketPrice.priceForAge(age));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -30})
    void negativeAgeThrows(int age) {
        assertThrows(IllegalArgumentException.class, () -> CinemaTicketPrice.priceForAge(age));
    }
}
