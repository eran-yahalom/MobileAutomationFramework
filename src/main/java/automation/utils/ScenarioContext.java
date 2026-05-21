package automation.utils;

import java.util.HashMap;
import java.util.Map;


public class ScenarioContext {

    private static ThreadLocal<Map<String, Object>> context =
            ThreadLocal.withInitial(HashMap::new);

    public static void save(String key, Object value) {
        context.get().put(key, value);
    }

    public static <T> T get(String key, Class<T> type) {
        Object value = context.get().get(key);

        if (value == null) {
            throw new RuntimeException("No value found in ScenarioContext for key: " + key);
        }

        return type.cast(value);
    }

    public static void clear() {
        context.get().clear();
        context.remove();
    }

    public static Object getOrDefault(String key, Object defaultValue) {
        return context.get().getOrDefault(key, defaultValue);
    }
}
