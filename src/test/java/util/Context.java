package util;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private static final Map<ContextKeys, Object> context = new HashMap<>();
    public static void setContext(ContextKeys key, Object value){
        context.put(key, value);
    }

    public static Object getContextValue(ContextKeys key){
        return context.get(key);
    }

    public static void clearContext(){
        context.clear();
    }
}
