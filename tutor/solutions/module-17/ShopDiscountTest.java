import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShopDiscount {
    // цена после скидки в процентах; скидка не больше 50%, копейки отбрасываем
    static int priceAfterDiscount(int price, int percent) {
        if (percent > 50) {
            percent = 50;
        }
        return price - price * percent / 100;
    }
}

public class ShopDiscountTest {

    @Test
    void tenPercentOffThousand() {
        assertEquals(900, ShopDiscount.priceAfterDiscount(1000, 10));
    }

    @Test
    void zeroPercentKeepsPrice() {
        assertEquals(500, ShopDiscount.priceAfterDiscount(500, 0));
    }

    @Test
    void discountAboveFiftyIsCut() {
        assertEquals(500, ShopDiscount.priceAfterDiscount(1000, 70), "скидка больше 50% должна обрезаться до 50%");
    }

    @Test
    void fiftyPercentIsAllowed() {
        assertEquals(50, ShopDiscount.priceAfterDiscount(100, 50));
    }
}
