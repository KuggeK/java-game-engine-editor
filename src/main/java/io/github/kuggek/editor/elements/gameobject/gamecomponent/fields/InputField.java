package io.github.kuggek.editor.elements.gameobject.gamecomponent.fields;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.layout.VBox;
import javafx.util.Duration;

public abstract class InputField extends VBox {

    private Class<?> type;
    private Object value;

    public final Duration TOOLTIP_SHOW_DELAY = new Duration(100);

    public InputField(Class<?> type) {
        this.type = type;
    }

    private EventHandler<ActionEvent> onValueChange;

    public abstract void setValue(Object value);

    public Object getValue() {
        return value;
    }

    public void setOnValueChange(EventHandler<ActionEvent> eventHandler) {
        onValueChange = eventHandler;
    }

    public EventHandler<ActionEvent> getOnValueChange() {
        return onValueChange;
    }

    protected void handleValueChange(ActionEvent e) {
        if (onValueChange != null) {
            onValueChange.handle(e);
        }
    }
}
