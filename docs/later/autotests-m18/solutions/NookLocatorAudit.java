import java.util.Scanner;

// m18-t04 — эталон. Мастерская: правила проверяются по порядку, первое сработавшее решает.
public class NookLocatorAudit {

    /** Сколько «шагов» в локаторе: для XPath — частей между /, для CSS — частей между пробелами и >. */
    static int countSteps(String locator) {
        String[] parts = locator.startsWith("/")
                ? locator.split("/+")
                : locator.split("[\\s>]+");
        int steps = 0;
        for (String p : parts) {
            if (!p.isEmpty()) {
                steps++;
            }
        }
        return steps;
    }

    /** Есть ли в локаторе номер элемента: [цифры] или nth-. */
    static boolean hasIndex(String locator) {
        if (locator.contains("nth-")) {
            return true;
        }
        for (int i = 0; i < locator.length(); i++) {
            if (locator.charAt(i) == '[') {
                int j = i + 1;
                while (j < locator.length() && Character.isDigit(locator.charAt(j))) {
                    j++;
                }
                if (j > i + 1 && j < locator.length() && locator.charAt(j) == ']') {
                    return true;
                }
            }
        }
        return false;
    }

    static String verdict(String locator) {
        if (locator.startsWith("/") && !locator.startsWith("//")) {
            return "ХРУПКИЙ: абсолютный путь";
        }
        if (hasIndex(locator)) {
            return "ХРУПКИЙ: номер элемента";
        }
        if (locator.contains("data-test-id")) {
            return "СТАБИЛЬНЫЙ: тестовый атрибут";
        }
        int steps = countSteps(locator);
        if (locator.startsWith("#") && steps == 1) {
            return "СТАБИЛЬНЫЙ: id";
        }
        if (steps > 3) {
            return "ХРУПКИЙ: длинная цепочка (шагов: " + steps + ")";
        }
        if (locator.contains("text()")) {
            return "НОРМАЛЬНЫЙ: зависит от текста";
        }
        return "НОРМАЛЬНЫЙ: обычный атрибут или класс";
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int stable = 0;
        int normal = 0;
        int fragile = 0;
        while (in.hasNextLine()) {
            String locator = in.nextLine().trim();
            if (locator.isEmpty()) {
                continue;
            }
            String v = verdict(locator);
            System.out.println(locator + " — " + v);
            if (v.startsWith("СТАБИЛЬНЫЙ")) {
                stable++;
            } else if (v.startsWith("НОРМАЛЬНЫЙ")) {
                normal++;
            } else {
                fragile++;
            }
        }
        System.out.println("Итого: стабильных " + stable + ", нормальных " + normal + ", хрупких " + fragile);
    }
}
