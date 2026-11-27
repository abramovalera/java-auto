import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Задача m18-t07. Охотник за багами: твои проверки должны пропускать правильную скидку
// и ловить каждую из четырёх «испорченных» версий (мутантов).
public class NookDiscountBugHunterTest {

    /**
     * ТВОЯ ЧАСТЬ. Проверь правило скидки по требованиям из условия задачи.
     * Пиши проверки AssertJ: assertThat(rule.percent(сумма, постоянный)).isEqualTo(ожидание);
     * Для исключения: assertThatThrownBy(() -> rule.percent(...)).isInstanceOf(...);
     */
    static void checkDiscountRule(NookDiscountRule rule) {
        // TODO: твои проверки
    }

    // ===================== НЕ МЕНЯЙ КОД НИЖЕ =====================
    // Готовые тесты: правильное правило должно пройти твои проверки, каждый мутант — провалить их.

    @Test
    void correctRulePassesYourChecks() {
        checkDiscountRule(new NookDiscountCorrect());
    }

    @Test
    void catchesMutantA() {
        assertThatThrownBy(() -> checkDiscountRule(new NookDiscountMutantA())).isInstanceOf(AssertionError.class);
    }

    @Test
    void catchesMutantB() {
        assertThatThrownBy(() -> checkDiscountRule(new NookDiscountMutantB())).isInstanceOf(AssertionError.class);
    }

    @Test
    void catchesMutantC() {
        assertThatThrownBy(() -> checkDiscountRule(new NookDiscountMutantC())).isInstanceOf(AssertionError.class);
    }

    @Test
    void catchesMutantD() {
        assertThatThrownBy(() -> checkDiscountRule(new NookDiscountMutantD())).isInstanceOf(AssertionError.class);
    }
}

/** Правило скидки книжного магазина Nook: процент скидки по сумме корзины. */
interface NookDiscountRule {
    int percent(int cartTotal, boolean loyalCustomer);
}

// Правильная реализация. Мутанты ниже отличаются от неё ровно одной ошибкой каждый.
// Совет: сначала составь проверки по требованиям, и только потом читай код мутантов.
class NookDiscountCorrect implements NookDiscountRule {
    public int percent(int cartTotal, boolean loyalCustomer) {
        if (cartTotal < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной: " + cartTotal);
        }
        int p = 0;
        if (cartTotal >= 5000) {
            p = 10;
        } else if (cartTotal >= 1000) {
            p = 5;
        }
        if (loyalCustomer) {
            p += 5;
        }
        return Math.min(p, 12);
    }
}

class NookDiscountMutantA implements NookDiscountRule {
    public int percent(int cartTotal, boolean loyalCustomer) {
        if (cartTotal < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной: " + cartTotal);
        }
        int p = 0;
        if (cartTotal >= 5000) {
            p = 10;
        } else if (cartTotal > 1000) {
            p = 5;
        }
        if (loyalCustomer) {
            p += 5;
        }
        return Math.min(p, 12);
    }
}

class NookDiscountMutantB implements NookDiscountRule {
    public int percent(int cartTotal, boolean loyalCustomer) {
        if (cartTotal < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной: " + cartTotal);
        }
        int p = 0;
        if (cartTotal >= 5000) {
            p = 10;
        } else if (cartTotal >= 1000) {
            p = 5;
        }
        if (loyalCustomer) {
            p += 5;
        }
        return p;
    }
}

class NookDiscountMutantC implements NookDiscountRule {
    public int percent(int cartTotal, boolean loyalCustomer) {
        if (cartTotal < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной: " + cartTotal);
        }
        int p = 0;
        if (cartTotal >= 5000) {
            p = 10;
        } else if (cartTotal >= 1000) {
            p = 5;
        }
        if (loyalCustomer && p > 0) {
            p += 5;
        }
        return Math.min(p, 12);
    }
}

class NookDiscountMutantD implements NookDiscountRule {
    public int percent(int cartTotal, boolean loyalCustomer) {
        if (cartTotal < 0) {
            return 0;
        }
        int p = 0;
        if (cartTotal >= 5000) {
            p = 10;
        } else if (cartTotal >= 1000) {
            p = 5;
        }
        if (loyalCustomer) {
            p += 5;
        }
        return Math.min(p, 12);
    }
}
