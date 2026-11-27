import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Крошечный JSON-парсер и сериализатор без библиотек.
 * Объект → LinkedHashMap, массив → ArrayList, строка → String,
 * целое число → Long, дробное → Double, true/false → Boolean, null → null.
 */
public final class Json {

    private final String s;
    private int pos;

    private Json(String s) {
        this.s = s;
    }

    /** Разобрать JSON-текст. Бросает IllegalArgumentException, если текст некорректен. */
    public static Object parse(String text) {
        Json p = new Json(text);
        p.skipWs();
        Object v = p.value();
        p.skipWs();
        if (p.pos != p.s.length()) throw p.error("лишние символы после JSON");
        return v;
    }

    private Object value() {
        if (pos >= s.length()) throw error("неожиданный конец");
        char c = s.charAt(pos);
        return switch (c) {
            case '{' -> object();
            case '[' -> array();
            case '"' -> string();
            case 't' -> literal("true", Boolean.TRUE);
            case 'f' -> literal("false", Boolean.FALSE);
            case 'n' -> literal("null", null);
            default -> {
                if (c == '-' || (c >= '0' && c <= '9')) yield number();
                throw error("неожиданный символ '" + c + "'");
            }
        };
    }

    private Map<String, Object> object() {
        Map<String, Object> m = new LinkedHashMap<>();
        pos++; // {
        skipWs();
        if (peek() == '}') { pos++; return m; }
        while (true) {
            skipWs();
            if (peek() != '"') throw error("ожидался ключ в кавычках");
            String key = string();
            skipWs();
            expect(':');
            skipWs();
            m.put(key, value());
            skipWs();
            char c = next();
            if (c == '}') return m;
            if (c != ',') throw error("ожидалась ',' или '}'");
        }
    }

    private List<Object> array() {
        List<Object> list = new ArrayList<>();
        pos++; // [
        skipWs();
        if (peek() == ']') { pos++; return list; }
        while (true) {
            skipWs();
            list.add(value());
            skipWs();
            char c = next();
            if (c == ']') return list;
            if (c != ',') throw error("ожидалась ',' или ']'");
        }
    }

    private String string() {
        pos++; // "
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            if (c == '"') return sb.toString();
            if (c == '\\') {
                char e = next();
                switch (e) {
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case '/' -> sb.append('/');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    case 'u' -> {
                        if (pos + 4 > s.length()) throw error("обрезанный \\u");
                        sb.append((char) Integer.parseInt(s.substring(pos, pos + 4), 16));
                        pos += 4;
                    }
                    default -> throw error("неизвестный спецсимвол \\" + e);
                }
            } else {
                sb.append(c);
            }
        }
    }

    private Object number() {
        int start = pos;
        if (peek() == '-') pos++;
        while (pos < s.length() && "0123456789.eE+-".indexOf(s.charAt(pos)) >= 0) pos++;
        String t = s.substring(start, pos);
        try {
            if (t.contains(".") || t.contains("e") || t.contains("E")) return Double.parseDouble(t);
            return Long.parseLong(t);
        } catch (NumberFormatException ex) {
            throw error("некорректное число " + t);
        }
    }

    private Object literal(String word, Object v) {
        if (!s.startsWith(word, pos)) throw error("ожидалось " + word);
        pos += word.length();
        return v;
    }

    private void skipWs() {
        while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) pos++;
    }

    private char peek() {
        return pos < s.length() ? s.charAt(pos) : '\0';
    }

    private char next() {
        if (pos >= s.length()) throw error("неожиданный конец");
        return s.charAt(pos++);
    }

    private void expect(char c) {
        if (next() != c) throw error("ожидался '" + c + "'");
    }

    private IllegalArgumentException error(String msg) {
        return new IllegalArgumentException("Некорректный JSON (позиция " + pos + "): " + msg);
    }

    // ------------------------------------------------------------------
    // Сериализация
    // ------------------------------------------------------------------

    public static String stringify(Object o) {
        if (o == null) return "null";
        if (o instanceof String str) return quote(str);
        if (o instanceof Double d) {
            return d == Math.rint(d) && !Double.isInfinite(d) ? String.valueOf(d.longValue()) : d.toString();
        }
        if (o instanceof Number || o instanceof Boolean) return o.toString();
        if (o instanceof Map<?, ?> map) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> e : map.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append(quote(String.valueOf(e.getKey()))).append(':').append(stringify(e.getValue()));
            }
            return sb.append('}').toString();
        }
        if (o instanceof Iterable<?> list) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : list) {
                if (!first) sb.append(',');
                first = false;
                sb.append(stringify(item));
            }
            return sb.append(']').toString();
        }
        return quote(o.toString());
    }

    public static String quote(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        return sb.append('"').toString();
    }
}
