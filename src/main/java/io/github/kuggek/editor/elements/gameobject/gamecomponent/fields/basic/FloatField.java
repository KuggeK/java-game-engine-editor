package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic;

import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.InputField;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;

public class FloatField extends InputField {
    
    private TextField textField;
    private Tooltip tooltip;

    private EventHandler<KeyEvent> handleNumber = e -> {
        String fullC = e.getCharacter();
        Character c = fullC.charAt(0);

        if (Character.isDigit(c) || c == '-' || c == '.') {
            return;
        }

        int caretPos = textField.getCaretPosition();
        textField.setText(textField.getText().replaceAll("[^\\d-.]", ""));
        textField.positionCaret(caretPos);
    };

    public FloatField() {
        super(Float.class);
        textField = new TextField();
        getChildren().add(textField);

        textField.setOnAction(this::handleValueChange);
        textField.setOnKeyTyped(handleNumber);

        textField.setPrefWidth(55);

        tooltip = new Tooltip("float");
        textField.setTooltip(tooltip);
        tooltip.setShowDelay(TOOLTIP_SHOW_DELAY);
    }

    @Override
    public Float getValue() {
        Float value = 0.0f;
        try {
            value = Float.parseFloat(textField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid float value: " + textField.getText());
            textField.setText("0.0");
        }
        return value;
    }

    @Override
    public void setValue(Object value) {
        try {
            textField.setText(Float.parseFloat(value.toString()) + "");
        } catch (Exception e) {
            setValue(0.0f);       
        }
    }

    public void setValue(Float value) {
        textField.setText(value.toString());
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
