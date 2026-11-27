import java.util.Scanner;

// m18-t03 — эталон. Page Object на заглушках: страницы прячут локаторы и шаги, main читается как тест-кейс.
public class NookLoginFlow {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String user = in.nextLine();
        String password = in.nextLine();
        String expected = in.nextLine().trim();

        NookFakeSite site = new NookFakeSite();
        NookLoginPage loginPage = new NookLoginPage(site).open();
        if (expected.equals("успех")) {
            NookCatalogPage catalog = loginPage.loginAs(user, password);
            System.out.println("Итог: " + catalog.greeting());
        } else {
            String error = loginPage.loginExpectingError(user, password).errorText();
            System.out.println("Итог: ошибка «" + error + "»");
        }
    }
}

class NookLoginPage {
    private static final String LOGIN_FIELD = "[data-test-id='login']";
    private static final String PASSWORD_FIELD = "[data-test-id='password']";
    private static final String LOGIN_BUTTON = "[data-test-id='login-button']";

    private final NookFakeSite site;

    NookLoginPage(NookFakeSite site) {
        this.site = site;
    }

    NookLoginPage open() {
        site.open("/login");
        System.out.println("[LoginPage] открыть /login");
        return this;
    }

    NookCatalogPage loginAs(String user, String password) {
        fillAndSubmit(user, password);
        return new NookCatalogPage(site);
    }

    NookLoginPage loginExpectingError(String user, String password) {
        fillAndSubmit(user, password);
        return this;
    }

    String errorText() {
        return site.errorText();
    }

    private void fillAndSubmit(String user, String password) {
        site.type(LOGIN_FIELD, user);
        System.out.println("[LoginPage] ввод в " + LOGIN_FIELD + ": " + user);
        site.type(PASSWORD_FIELD, password);
        System.out.println("[LoginPage] ввод в " + PASSWORD_FIELD + ": " + "*".repeat(password.length()));
        site.click(LOGIN_BUTTON);
        System.out.println("[LoginPage] клик по " + LOGIN_BUTTON);
    }
}

class NookCatalogPage {
    private final NookFakeSite site;

    NookCatalogPage(NookFakeSite site) {
        this.site = site;
        System.out.println("[CatalogPage] открыта страница " + site.currentPath());
    }

    String greeting() {
        return site.greeting();
    }
}

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
