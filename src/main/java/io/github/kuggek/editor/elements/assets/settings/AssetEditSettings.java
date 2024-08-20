package io.github.kuggek.editor.elements.assets.settings;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;

public abstract class AssetEditSettings<T extends Event> extends VBox {

    private EventHandler<T> onSave; 
    private EventHandler<T> onDelete;
    private EventHandler<T> onClose;

    public AssetEditSettings() {
        super();
    }

    public EventHandler<T> getOnSave() {
        return onSave;
    }

    public void setOnSave(EventHandler<T> onSave) {
        this.onSave = onSave;
    }

    public void setOnDelete(EventHandler<T> onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnClose(EventHandler<T> onClose) {
        this.onClose = onClose;
    }

    public abstract void save();
    public abstract void delete();
    public abstract void close();

    protected void handleOnSave(T event) {
        if (onSave != null) {
            onSave.handle(event);
        }
    }

    protected void handleOnDelete(T event) {
        if (onDelete != null) {
            onDelete.handle(event);
        }
    }

    protected void handleOnClose(T event) {
        if (onClose != null) {
            onClose.handle(event);
        }
    }
}
