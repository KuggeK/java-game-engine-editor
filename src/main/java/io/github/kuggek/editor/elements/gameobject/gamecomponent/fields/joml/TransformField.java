package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.joml;

import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.InputField;
import io.github.kuggek.engine.core.Transform;

public class TransformField extends InputField {
    
    private Transform transform;

    private Vector3fField position;
    private QuaternionfField rotation;
    private Vector3fField scale;

    public TransformField() {
        super(Transform.class);

        position = new Vector3fField();
        rotation = new QuaternionfField();
        scale = new Vector3fField();

        getChildren().addAll(position, rotation, scale);

        position.setOnValueChange(e -> {
            transform.setPosition(position.getValue());
            handleValueChange(e);
        });
        rotation.setOnValueChange(e -> {
            transform.setRotation(rotation.getValue());
            handleValueChange(e);
        });
        scale.setOnValueChange(e -> {
            transform.setScale(scale.getValue());
            handleValueChange(e);
        });
    }

    public void setValue(Transform transform) {
        this.transform = transform;
        position.setValue(transform.getPosition());
        rotation.setValue(transform.getRotation());
        scale.setValue(transform.getScale());
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Transform) {
            setValue((Transform) value);
        }
    }

    @Override
    public Transform getValue() {
        return transform;
    }
}
