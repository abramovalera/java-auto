import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

// m18-t05 — эталон. Одна логика теста — много строк данных.
public class NookEmailRuleTest {

    @ParameterizedTest
    @ValueSource(strings = {"anna@mail.ru", "a@b.co", "anna.petrova@nook.shop", "anna+books@mail.co.uk", "ANNA@MAIL.RU"})
    void validEmailsAreAccepted(String email) {
        assertThat(NookEmailRule.isValid(email)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "anna", "@mail.ru", "anna@", "anna@mail", "anna@.ru", "anna@mail.",
            "an na@mail.ru", "anna@@mail.ru", "anna@mail@ru.ru"})
    void invalidEmailsAreRejected(String email) {
        assertThat(NookEmailRule.isValid(email)).isFalse();
    }

    @Test
    void nullIsNotAnEmail() {
        assertThat(NookEmailRule.isValid(null)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "1,  true",
            "59, true",
            "60, false",
            "70, false"
    })
    void lengthBoundary(int localPartLength, boolean expected) {
        String email = "a".repeat(localPartLength) + "@m.ru";
        assertThat(NookEmailRule.isValid(email)).isEqualTo(expected);
    }
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
