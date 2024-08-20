package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.joml;

import org.joml.Quaternionf;

import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.InputField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic.FloatField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class QuaternionfField extends InputField {
    private Quaternionf quaternion;

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
        quaternion.x = x.getValue();
        quaternion.y = y.getValue();
        quaternion.z = z.getValue();
        quaternion.w = w.getValue();
        quaternion.normalize();
        updateFields();

        handleValueChange(e);
    };

    public QuaternionfField() {
        super(Quaternionf.class);
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

    public void setValue(Quaternionf quaternion) {
        this.quaternion = quaternion;
        updateFields();
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Quaternionf) {
            setValue((Quaternionf) value);
        }
    }

    public void updateFields() {
        x.setValue(String.valueOf(quaternion.x));
        y.setValue(String.valueOf(quaternion.y));
        z.setValue(String.valueOf(quaternion.z));
        w.setValue(String.valueOf(quaternion.w));
    }

    @Override
    public Quaternionf getValue() {
        return quaternion;
    }
}
