package io.github.kuggek.editor.elements.gameobject.gamecomponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * This class is used to cast strings to values of different types.
 * It is used to convert the string representation of a value to the actual value.
 */
public class StringCasting {
    
    private static final Map<Class<?>, Function<String, Object>> casters = new HashMap<>();

    static {
        casters.put(String.class, s -> s);
        casters.put(Integer.class, Integer::parseInt);
        casters.put(int.class, Integer::parseInt);
        casters.put(Float.class, Float::parseFloat);
        casters.put(float.class, Float::parseFloat);
        casters.put(Double.class, Double::parseDouble);
        casters.put(double.class, Double::parseDouble);
        casters.put(Boolean.class, Boolean::parseBoolean);
        casters.put(boolean.class, Boolean::parseBoolean);
        casters.put(Vector3f.class, s -> {
            String[] split = s.split(",");
            return new Vector3f(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
        });
        casters.put(Vector4f.class, s -> {
            String[] split = s.split(",");
            return new Vector4f(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]));
        });
    }

    /**
     * Casts a string to a value of the given type.
     * @param type The type to cast to
     * @param value The string to cast
     * @return The casted value
     */
    public static <T> T cast(Class<T> type, String value) throws ClassCastException {
        Object casted = casters.get(type).apply(value);
        return (T)casted;
    }
}
