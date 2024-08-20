package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic;

import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.InputField;
import javafx.scene.control.CheckBox;

public class BooleanField extends InputField {

    private CheckBox checkBox;
    
    public BooleanField() {
        super(Boolean.class);

        checkBox = new CheckBox();
        getChildren().add(checkBox);
        checkBox.setOnAction(this::handleValueChange);
    }

    @Override
    public Boolean getValue() {
        return checkBox.isSelected();
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Boolean) {
            checkBox.setSelected((Boolean) value);
        }
    }
}
