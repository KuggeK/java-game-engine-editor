package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields;

import java.util.HashMap;
import java.util.Map;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic.BooleanField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic.FloatField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic.IntField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic.StringField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.joml.QuaternionfField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.joml.TransformField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.joml.Vector3fField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.joml.Vector4fField;
import io.github.kuggek.engine.core.Transform;

public class InputFieldTypes {
    
    private static final HashMap<Class<?>, Class<? extends InputField>> inputFieldTypes = new HashMap<>();

    static {
        inputFieldTypes.put(Transform.class, TransformField.class);
        inputFieldTypes.put(String.class, StringField.class); 
        inputFieldTypes.put(Boolean.class, BooleanField.class);
        inputFieldTypes.put(boolean.class, BooleanField.class);
        inputFieldTypes.put(Vector3f.class, Vector3fField.class);
        inputFieldTypes.put(Vector4f.class, Vector4fField.class);
        inputFieldTypes.put(Quaternionf.class, QuaternionfField.class);
        inputFieldTypes.put(Integer.class, IntField.class);
        inputFieldTypes.put(int.class, IntField.class);
        inputFieldTypes.put(Float.class, FloatField.class);
        inputFieldTypes.put(float.class, FloatField.class);
    }

    public static Class<? extends InputField> getInputField(Class<?> type) {
        return inputFieldTypes.getOrDefault(type, GenericTextField.class);
    }

    public static void registerInputField(Class<?> type, Class<? extends InputField> field) {
        inputFieldTypes.put(type, field);
    }

    public static void unregisterInputField(Class<?> type) {
        inputFieldTypes.remove(type);
    }

    public static Map<Class<?>, Class<? extends InputField>> getInputFieldTypes() {
        return Map.copyOf(inputFieldTypes);
    }
}
