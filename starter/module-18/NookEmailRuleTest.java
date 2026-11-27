import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

// Задача m18-t05. Параметризованные тесты для правила проверки email (класс NookEmailRule ниже).
public class NookEmailRuleTest {

    // TODO 1: @ParameterizedTest + @ValueSource — правильные адреса (isValid → true)

    // TODO 2: @ParameterizedTest + @ValueSource — неправильные адреса (isValid → false), включая пустую строку

    // TODO 3: @Test — null не адрес

    // TODO 4: @ParameterizedTest + @CsvSource — граница длины: таблица «длина до @ | ожидаемый результат»

}

// ===================== НЕ МЕНЯЙ КОД НИЖЕ =====================
// Правило, по которому форма регистрации магазина Nook проверяет email.
class NookEmailRule {
    /**
     * Адрес правильный, если: не null, не длиннее 64 символов, без пробелов,
     * ровно одна @ и перед ней хотя бы один символ,
     * после @ есть точка, и она не первая и не последняя в этой части.
     */
    static boolean isValid(String email) {
        if (email == null || email.length() > 64 || email.contains(" ")) {
            return false;
        }
        int at = email.indexOf('@');
        if (at <= 0 || at != email.lastIndexOf('@')) {
            return false;
        }
        String domain = email.substring(at + 1);
        int dot = domain.lastIndexOf('.');
        return dot > 0 && dot < domain.length() - 1;
    }
}
