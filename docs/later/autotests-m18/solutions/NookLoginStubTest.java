import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// m18-t02 — эталон. Каждый тест получает свежую страницу из @BeforeEach — тесты независимы.
public class NookLoginStubTest {

    private NookFakeLoginPage page;

    @BeforeEach
    void openLoginPage() {
        page = new NookFakeLoginPage();
    }

    @Test
    void validUserLogsIn() {
        page.login("anna", "qwerty123");
        assertThat(page.isLoggedIn()).isTrue();
        assertThat(page.getError()).isEmpty();
    }

    @Test
    void wrongPasswordShowsError() {
        page.login("anna", "12345");
        assertThat(page.isLoggedIn()).isFalse();
        assertThat(page.getError()).isEqualTo("Неверный логин или пароль");
    }

    @Test
    void emptyLoginShowsError() {
        page.login("", "qwerty123");
        assertThat(page.isLoggedIn()).isFalse();
        assertThat(page.getError()).isEqualTo("Введите логин");
    }

    @Test
    void emptyPasswordShowsError() {
        page.login("anna", "  ");
        assertThat(page.isLoggedIn()).isFalse();
        assertThat(page.getError()).isEqualTo("Введите пароль");
    }

    @Test
    void fourthAttemptIsBlockedEvenWithRightPassword() {
        page.login("anna", "1");
        page.login("anna", "2");
        page.login("anna", "3");
        page.login("anna", "qwerty123");
        assertThat(page.isLoggedIn()).isFalse();
        assertThat(page.getError()).startsWith("Слишком много попыток");
    }
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
