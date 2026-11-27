import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoffeeBonusPoints {
    static int pointsFor(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной: " + amount);
        }
        int points = amount / 100 * 5;
        if (amount >= 1000) {
            points = points * 2;
        }
        return points;
    }
}

public class CoffeeBonusPointsTest {

    @ParameterizedTest(name = "{0} ₽ → {1} баллов")
    @CsvSource({
            "0, 0",
            "99, 0",
            "100, 5",
            "250, 10",
            "999, 45",
            "1000, 100",
            "1250, 120"
    })
    void pointsByRules(int amount, int expected) {
        assertEquals(expected, CoffeeBonusPoints.pointsFor(amount));
    }

    @Test
    void negativeAmountThrows() {
        assertThrows(IllegalArgumentException.class, () -> CoffeeBonusPoints.pointsFor(-1));
    }
}
