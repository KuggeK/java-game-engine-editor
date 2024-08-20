package io.github.kuggek.editor.elements.gameobject;

import io.github.kuggek.engine.ecs.GameComponent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ComponentContextMenu extends ContextMenu {
    
    private final MenuItem deleteGO = new MenuItem("Delete");

    public ComponentContextMenu(GameComponent component, GameObjectInspector inspector) {
        deleteGO.setOnAction(e -> inspector.removeComponent(component));
        getItems().add(deleteGO);
    }


}
