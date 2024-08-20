package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic;


import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.InputField;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;

public class IntField extends InputField {
    
    private TextField textField;
    private Tooltip tooltip;

    private EventHandler<KeyEvent> handleNumber = e -> {
        String fullC = e.getCharacter();
        Character c = fullC.charAt(0);

        if (Character.isDigit(c) || c == '-') {
            return;
        }

        int caretPos = textField.getCaretPosition();
        textField.setText(textField.getText().replaceAll("[^\\d-]", ""));
        textField.positionCaret(caretPos);
    };

    public IntField() {
        super(Integer.class);
        textField = new TextField();
        getChildren().add(textField);

        textField.setOnAction(this::handleValueChange);
        textField.setOnKeyTyped(handleNumber);

        textField.setPrefWidth(55);

        tooltip = new Tooltip("integer");
        textField.setTooltip(tooltip);
        tooltip.setShowDelay(TOOLTIP_SHOW_DELAY);
    }

    @Override
    public Integer getValue() {
        Integer value = 0;
        try {
            value = Integer.parseInt(textField.getText());
            textField.setText(value.toString());
        } catch (NumberFormatException e) {
            textField.setText("0");
        }
        return value;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Integer) {
            setValue((Integer) value);
        }
    }

    public void setValue(Integer value) {
        if (value == null) {
            value = 0;
        } 
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
