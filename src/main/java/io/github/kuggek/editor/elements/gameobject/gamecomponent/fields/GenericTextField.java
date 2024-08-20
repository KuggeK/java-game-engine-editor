package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields;

import io.github.kuggek.editor.elements.gameobject.gamecomponent.StringCasting;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class GenericTextField extends InputField {
    private TextField textField;
    private Object value;
    private Tooltip tooltip;
    
    public GenericTextField() {
        super(Object.class);
        textField = new TextField();
        getChildren().add(textField);

        textField.setOnAction(this::handleValueChange);
        tooltip = new Tooltip();
        textField.setTooltip(tooltip);
        tooltip.setShowDelay(TOOLTIP_SHOW_DELAY);
    }

    @Override
    public Object getValue() {
        return StringCasting.cast(value.getClass(), textField.getText());
    }

    @Override
    public void setValue(Object value) {
        textField.setText(value.toString());
        tooltip.setText(value.getClass().getName());
    }

    @Override
    public void disable() {
        textField.setDisable(true);
    }

    @Override
    public void enable() {
        textField.setDisable(false);
    }
}
