package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.joml;

import org.joml.Vector3f;

import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.InputField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic.FloatField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class Vector3fField extends InputField {
    private Vector3f vector;

    private HBox container = new HBox();
    
    private Label xLabel = new Label("x");
    private FloatField x = new FloatField();

    private Region separator1 = new Region();

    private Label yLabel = new Label("y");
    private FloatField y = new FloatField();

    private Region separator2 = new Region();

    private Label zLabel = new Label("z");
    private FloatField z = new FloatField();

    private EventHandler<ActionEvent> onFieldChange = e -> {
        vector.x = x.getValue();
        vector.y = y.getValue();
        vector.z = z.getValue();

        handleValueChange(e);
    };

    public Vector3fField() {
        super(Vector3f.class);
        container.getChildren().addAll(xLabel, x, separator1, yLabel, y, separator2, zLabel, z);

        getChildren().add(container);

        x.setOnValueChange(onFieldChange);
        y.setOnValueChange(onFieldChange);
        z.setOnValueChange(onFieldChange);

        HBox.setHgrow(separator1, Priority.ALWAYS);
        HBox.setHgrow(separator2, Priority.ALWAYS);
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Vector3f) {
            vector = (Vector3f) value;
            updateFields();
        }
    }

    public void setValue(Vector3f vector) {
        this.vector = vector;
        updateFields();
    }

    public void updateFields() {
        x.setValue(vector.x);
        y.setValue(vector.y);
        z.setValue(vector.z);
    }

    @Override
    public Vector3f getValue() {
        return vector;
    }

    @Override
    public void disable() {
        x.disable();
        y.disable();
        z.disable();
    }

    @Override
    public void enable() {
        x.enable();
        y.enable();
        z.enable();
    }
}
