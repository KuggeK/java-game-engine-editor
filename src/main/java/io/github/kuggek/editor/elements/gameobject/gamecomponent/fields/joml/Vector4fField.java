package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.joml;

import org.joml.Vector4f;

import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.InputField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic.FloatField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class Vector4fField extends InputField {
    private Vector4f vector;

    private HBox container = new HBox();

    private Label xLabel = new Label("x");
    private FloatField x = new FloatField();

    private Region separator1 = new Region();

    private Label yLabel = new Label("y");
    private FloatField y = new FloatField();

    private Region separator2 = new Region();

    private Label zLabel = new Label("z");
    private FloatField z = new FloatField();

    private Region separator3 = new Region();

    private Label wLabel = new Label("w");
    private FloatField w = new FloatField();

    private EventHandler<ActionEvent> onFieldChange = e -> {
        vector.x = x.getValue();
        vector.y = y.getValue();
        vector.z = z.getValue();
        vector.w = w.getValue();

        handleValueChange(e);
    };

    public Vector4fField() {
        super(Vector4f.class);
        container.getChildren().addAll(xLabel, x, separator1, yLabel, y, separator2, zLabel, z, wLabel, w);
        getChildren().add(container);

        x.setOnValueChange(onFieldChange);
        y.setOnValueChange(onFieldChange);
        z.setOnValueChange(onFieldChange);
        w.setOnValueChange(onFieldChange);

        HBox.setHgrow(separator1, Priority.ALWAYS);
        HBox.setHgrow(separator2, Priority.ALWAYS);
        HBox.setHgrow(separator3, Priority.ALWAYS);
    }

    public void setValue(Vector4f vector) {
        this.vector = vector;
        updateFields();
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Vector4f) {
            setValue((Vector4f) value);
        }
    }

    public void updateFields() {
        x.setValue(vector.x);
        y.setValue(vector.y);
        z.setValue(vector.z);
        w.setValue(vector.w);
    }

    @Override
    public Vector4f getValue() {
        return vector;
    }
}
