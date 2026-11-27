import java.util.Scanner;

// Задача m18-t03. Page Object на заглушках.
// Напиши классы NookLoginPage и NookCatalogPage (под этим классом) и сценарий в main.
public class NookLoginFlow {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // TODO: прочитай логин, пароль и ожидание («успех» или «ошибка»)
        // TODO: создай сайт NookFakeSite и страницу входа, пройди сценарий, напечатай «Итог: …»
    }
}

// TODO: class NookLoginPage { … }

// TODO: class NookCatalogPage { … }

// ===================== НЕ МЕНЯЙ КОД НИЖЕ =====================
// «Сайт» книжного магазина Nook — заглушка вместо браузера. Ничего не печатает, только помнит состояние.
// Страницы (Page Object) работают с сайтом через его методы, как настоящие страницы — через Selenide.
class NookFakeSite {
    private String path = "about:blank";
    private String login = "";
    private String password = "";
    private String error = "";

    /** Открыть адрес, например "/login". */
    void open(String path) {
        this.path = path;
        error = "";
    }

    /** Ввести текст в поле с локатором. */
    void type(String locator, String text) {
        if (locator.equals("[data-test-id='login']")) {
            login = text;
        } else if (locator.equals("[data-test-id='password']")) {
            password = text;
        }
    }

    /** Нажать на элемент с локатором. */
    void click(String locator) {
        if (!path.equals("/login") || !locator.equals("[data-test-id='login-button']")) {
            return;
        }
        if (login.isBlank()) {
            error = "Введите логин";
        } else if (password.isBlank()) {
            error = "Введите пароль";
        } else if (login.equals("blocked")) {
            error = "Пользователь заблокирован";
        } else if ((login.equals("anna") && password.equals("qwerty123"))
                || (login.equals("boris") && password.equals("bookworm"))) {
            error = "";
            path = "/catalog";
        } else {
            error = "Неверный логин или пароль";
        }
    }

    /** Адрес открытой страницы. */
    String currentPath() {
        return path;
    }

    /** Текст ошибки на странице входа ("" — ошибки нет). */
    String errorText() {
        return error;
    }

    /** Приветствие в шапке каталога; если каталог не открыт — пометка об этом. */
    String greeting() {
        return path.equals("/catalog") ? "Привет, " + login + "!" : "(каталог не открыт)";
    }
}
