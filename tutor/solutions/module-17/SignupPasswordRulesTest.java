import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignupPasswordRules {
    // надёжный пароль: не короче 8 символов, есть цифра и заглавная буква
    static boolean isStrong(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Пароль не может быть null");
        }
        if (password.length() < 8) {
            return false;
        }
        boolean hasDigit = false;
        boolean hasUpper = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }
        }
        return hasDigit && hasUpper;
    }
}

public class SignupPasswordRulesTest {

    @Test
    void goodPasswordIsStrong() {
        assertTrue(SignupPasswordRules.isStrong("Summer2024"), "длинный пароль с цифрой и заглавной — надёжный");
    }

    @Test
    void exactlyEightCharsIsEnough() {
        assertTrue(SignupPasswordRules.isStrong("Abcdef12"), "8 символов — уже можно");
    }

    @Test
    void sevenCharsIsTooShort() {
        assertFalse(SignupPasswordRules.isStrong("Abcde12"), "7 символов — слишком короткий");
    }

    @Test
    void withoutDigitIsWeak() {
        assertFalse(SignupPasswordRules.isStrong("SummerTime"), "без цифры — слабый");
    }

    @Test
    void withoutUppercaseIsWeak() {
        assertFalse(SignupPasswordRules.isStrong("summer2024"), "без заглавной — слабый");
    }

    @Test
    void nullPasswordThrows() {
        assertThrows(IllegalArgumentException.class, () -> SignupPasswordRules.isStrong(null));
    }
}
