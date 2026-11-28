import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// Задача m18-t02. Напиши здесь JUnit-тесты для «страницы входа» NookFakeLoginPage (она ниже).
// Если IDEA подчёркивает @Test красным — подключи JUnit 5 и AssertJ (см. условие задачи).
public class NookLoginStubTest {

    // TODO: поле со страницей и метод с @BeforeEach, который создаёт новую страницу перед каждым тестом

    // TODO: тесты с @Test — минимум четыре, каждый проверяет один сценарий

}

// ===================== НЕ МЕНЯЙ КОД НИЖЕ =====================
// «Страница входа» книжного магазина Nook — заглушка на обычной Java.
// Настоящая страница жила бы в браузере, а эта имитирует её поведение.
class NookFakeLoginPage {
    private String error = "";
    private boolean loggedIn = false;
    private int failedAttempts = 0;

    /** Ввести логин и пароль и нажать «Войти». */
    void login(String user, String password) {
        if (failedAttempts >= 3) {
            error = "Слишком много попыток. Попробуйте через 5 минут";
            return;
        }
        if (user.isBlank()) {
            error = "Введите логин";
            return;
        }
        if (password.isBlank()) {
            error = "Введите пароль";
            return;
        }
        if (user.equals("anna") && password.equals("qwerty123")) {
            loggedIn = true;
            error = "";
        } else {
            failedAttempts++;
            error = "Неверный логин или пароль";
        }
    }

    /** Текст красной ошибки под формой; пустая строка, если ошибки нет. */
    String getError() {
        return error;
    }

    /** Вошёл ли пользователь (на настоящем сайте — открылся ли каталог). */
    boolean isLoggedIn() {
        return loggedIn;
    }
}
