package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic;

import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.InputField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 * Not to be confused with Springfield, the city in the Simpsons.
 */
public class StringField extends InputField {
    
    private TextField textField;
    
    public StringField() {
        super(String.class);
        textField = new TextField();
        getChildren().add(textField);

        textField.setOnAction(this::handleValueChange);
        Tooltip tooltip = new Tooltip("String");
        textField.setTooltip(tooltip);
        tooltip.setShowDelay(TOOLTIP_SHOW_DELAY);        
    }

    @Override
    public String getValue() {
        return textField.getText();
    }

    @Override
    public void setValue(Object value) {
        textField.setText(value.toString());
    }

    public void setValue(String value) {
        textField.setText(value);
    }

}
