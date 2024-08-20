package io.github.kuggek.editor.elements.gameobject.gamecomponent;

import java.lang.reflect.Field;

import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.InputField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.InputFieldTypes;
import io.github.kuggek.engine.ecs.GameComponent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A field in the inspector for a component. Allows editing of the field value.
 */
public class ComponentUIField extends HBox {

    private GameComponent component;
    private Field field;

    private Label fieldName;
    private InputField fieldValue;

    private Region filler;

    private boolean error = false;

    public ComponentUIField(Field componentField, GameComponent gameComponent) {
        field = componentField;
        component = gameComponent;

        field.setAccessible(true);

        fieldName = new Label(field.getName());
        try {
            Object value = field.get(component);
            fieldValue = InputFieldTypes.getInputField(field.getType()).getConstructor().newInstance();
            fieldValue.setValue(value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.out.println("Error getting field value: " + e.getMessage());
            fieldValue = null;
        } catch (Exception e) {
            System.out.println("Error creating input field " + field.getName() + ": " + e.getMessage());
        }
        Tooltip tooltip = new Tooltip(field.getType().getName());
        tooltip.setShowDelay(new Duration(100));
        //fieldValue.setTooltip(tooltip);
        filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);

        getChildren().addAll(this.fieldName, filler, this.fieldValue);

        fieldValue.setOnValueChange(e -> {
            try {
                Object value = fieldValue.getValue();
                field.set(component, value);
                if (error) {
                    fieldValue.setBorder(null);
                    error = false;
                }
            } catch (Exception e1) {
                System.out.println("Error setting field value: " + e1.getMessage());
                fieldValue.setBorder(Border.stroke(Color.RED));
            }
        });
    }
}