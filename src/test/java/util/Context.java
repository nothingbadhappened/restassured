package util;

import enums.ContextKey;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private static final Map<ContextKey, Object> context = new HashMap<>();
    public static void setContext(ContextKey key, Object value){
        context.put(key, value);
    }

    public static Object getContextValue(ContextKey key){
        return context.get(key);
    }

    public static void clearContext(){
        context.clear();
    }
}
