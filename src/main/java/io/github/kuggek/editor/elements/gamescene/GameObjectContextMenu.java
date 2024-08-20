package io.github.kuggek.editor.elements.gamescene;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class GameObjectContextMenu extends ContextMenu {
    
    private final MenuItem deleteGO = new MenuItem("Delete");
    private final MenuItem addChildGO = new MenuItem("Add child");
    private final MenuItem disableGO = new MenuItem("Disable");

    private GameObjectDropDown go;

    public GameObjectContextMenu(GameObjectDropDown gameObject, GameSceneInspector inspector) {
        this.go = gameObject;

        deleteGO.setOnAction(e -> inspector.removeGameObject(gameObject));
        addChildGO.setOnAction(e -> {
            GameObjectDropDown newGO = inspector.addGameObject();
            inspector.createParentLink(gameObject, newGO);
        });

        if (gameObject.getGameObject().isDisabled()) {
            disableGO.setText("Enable");
        }

        disableGO.setOnAction(e -> {
            inspector.toggleGameObject(gameObject);
            if (gameObject.getGameObject().isDisabled()) {
                disableGO.setText("Enable");
            } else {
                disableGO.setText("Disable");
            }
        });

        getItems().addAll(deleteGO, addChildGO, disableGO);
    }

    public void update() {
        if (go.getGameObject().isDisabled()) {
            disableGO.setText("Enable");
        } else {
            disableGO.setText("Disable");
        }
    }
}
