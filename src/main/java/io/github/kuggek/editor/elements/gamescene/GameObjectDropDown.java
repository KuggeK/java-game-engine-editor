package io.github.kuggek.editor.elements.gamescene;

import io.github.kuggek.editor.elements.DropDownElement;
import io.github.kuggek.editor.elements.gameobject.GameObjectEvent;
import io.github.kuggek.engine.ecs.GameObject;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

public class GameObjectDropDown extends DropDownElement<VBox> {
    private GameObject gameObject;

    private VBox ddVBox;

    /**
     * Constructor for a GameObjectDropDown element. Depending on the recursive parameter, 
     * the dropdown will only create itself or recursively create the whole chain of 
     * GameObjectDropDowns from the GameObject to its children (and grandchildren).
     * @param gameObject The GameObject to create the dropdown for
     * @param recursive Whether to create the dropdown recursively
     */
    public GameObjectDropDown(GameObject gameObject, boolean recursive) {
        super(gameObject.getID() + ": " + gameObject.getName());
        setDropDownMargin(15);
        ddVBox = new VBox();

        this.gameObject = gameObject;

        update(false);

        if (recursive) {
            for (GameObject child : gameObject.getChildren()) {
                GameObjectDropDown childDropDown = new GameObjectDropDown(child, true);
                ddVBox.getChildren().add(childDropDown);
            }

            if (ddVBox.getChildren().size() > 0) {
                setDdElement(ddVBox);
            }
        }
    }

    private void setOnGameObjectSelected(EventHandler<GameObjectEvent> handler) {
        header.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                handler.handle(new GameObjectEvent(GameObjectEvent.GAME_OBJECT_SELECTED, gameObject));
            }
            e.consume();
        });
    }

    public void setOnGameObjectSelected(boolean recursive, EventHandler<GameObjectEvent> handler) {
        setOnGameObjectSelected(handler);

        if (recursive) {
            for (Node child : ddVBox.getChildren()) {
                if (child instanceof GameObjectDropDown) {
                    ((GameObjectDropDown) child).setOnGameObjectSelected(true, handler);
                }
            }
        }
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public VBox getDropdown() {
        return ddVBox;
    }

    public void addChild(GameObject child) {
        GameObjectDropDown childDropDown = new GameObjectDropDown(child, true);
        ddVBox.getChildren().add(childDropDown);
        setDdElement(ddVBox);
    }

    public void addChild(GameObjectDropDown child) {
        ddVBox.getChildren().add(child);
        setDdElement(ddVBox);
    }

    public void update(boolean recursive) {
        if (recursive) {
            for (Node child : ddVBox.getChildren()) {
                if (child instanceof GameObjectDropDown) {
                    ((GameObjectDropDown) child).update(true);
                }
            }
        }

        setTitle(gameObject.getID() + ": " + gameObject.getName());
    }
}
