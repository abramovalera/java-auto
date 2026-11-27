import java.util.Scanner;

// m18-t01 — эталон. Построитель локаторов: каждая команда → свой метод, main только разбирает строки.
public class NookLocatorBuilder {

    static String byId(String id) {
        return "#" + id;
    }

    static String byClass(String cssClass) {
        return "." + cssClass;
    }

    static String byTestId(String testId) {
        return "[data-test-id='" + testId + "']";
    }

    static String byAttr(String tag, String attr, String value) {
        return tag + "[" + attr + "='" + value + "']";
    }

    static String xpathByTestId(String testId) {
        return "//*[@data-test-id='" + testId + "']";
    }

    static String xpathByText(String tag, String text) {
        return "//" + tag + "[text()='" + text + "']";
    }

    static String xpathContainsText(String tag, String text) {
        return "//" + tag + "[contains(text(),'" + text + "')]";
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(" ", 2);
            String type = parts[0];
            String rest = parts.length > 1 ? parts[1].trim() : "";
            String result = switch (type) {
                case "id" -> byId(rest);
                case "class" -> byClass(rest);
                case "testid" -> byTestId(rest);
                case "xtestid" -> xpathByTestId(rest);
                case "attr" -> {
                    String[] p = rest.split(" ", 3);
                    yield byAttr(p[0], p[1], p[2]);
                }
                case "xtext" -> {
                    String[] p = rest.split(" ", 2);
                    yield xpathByText(p[0], p[1]);
                }
                case "xcontains" -> {
                    String[] p = rest.split(" ", 2);
                    yield xpathContainsText(p[0], p[1]);
                }
                default -> "? не знаю тип: " + type;
            };
            System.out.println(result);
        }
    }
}
