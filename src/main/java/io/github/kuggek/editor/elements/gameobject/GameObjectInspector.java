package io.github.kuggek.editor.elements.gameobject;

import java.util.Map;

import io.github.kuggek.editor.elements.DropDownElement;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.ComponentInspector;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.GOTagsEditorField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic.StringField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.joml.TransformField;
import io.github.kuggek.engine.ecs.GameComponent;
import io.github.kuggek.engine.ecs.GameObject;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GameObjectInspector extends VBox {
    private GameObject gameObject;

    private final String EMPTY_TEXT = "No GameObject selected";

    private final Label title;
    private final StringField nameField;
    private final CheckBox active;
    private final HBox headerContainer;

    private final GOTagsEditorField tags;

    private final Button addComponentButton;

    private final DropDownElement<TransformField> transformDD;
    private final TransformField transformField;

    private EventHandler<GameObjectEvent> onGameObjectToggle;

    public GameObjectInspector() {
        title = new Label(EMPTY_TEXT);
        nameField = new StringField();
        active = new CheckBox();
        headerContainer = new HBox();
        headerContainer.getChildren().add(title);
        getChildren().add(headerContainer);

        transformDD = new DropDownElement<>("Transform");
        transformField = new TransformField();
        transformDD.setDdElement(transformField);
        getChildren().add(transformField);

        tags = new GOTagsEditorField();
        VBox.setVgrow(tags, Priority.NEVER);

        addComponentButton = new Button("Add Component");
        addComponentButton.setOnAction(e -> openComponentSelector());
        VBox.setVgrow(addComponentButton, Priority.NEVER);
    }

    public void selectGameObject(GameObject go) {
        gameObject = go;
        if (gameObject == null) {
            setToEmpty();
            return;
        }

        removeComponents();

        nameField.setValue(gameObject.getName());
        nameField.setOnValueChange(e -> {
            gameObject.setName(nameField.getValue());
        });

        active.setSelected(!gameObject.isDisabled());
        active.setOnAction(e -> toggleGameObject());
        headerContainer.getChildren().addAll(nameField, active);

        transformField.setValue(gameObject.getTransform());
        getChildren().add(transformDD);
        transformDD.open();

        tags.setGO(gameObject);
        getChildren().add(tags);

        title.setText("GameObject " + gameObject.getID() + ": ");
        getChildren().add(addComponentButton);
        
        gameObject.getComponents().forEach(this::addComponentElement);
    }

    public void removeComponent(GameComponent component) {
        gameObject.removeComponent(component);
        getChildren().removeIf(e -> e instanceof ComponentInspector && ((ComponentInspector) e).getComponent() == component);
    }

    /**
     * Sets the inspector selection to empty, clearing it and displaying a placeholder text.
     */
    public void setToEmpty() {
        gameObject = null;
        removeComponents();
        title.setText(EMPTY_TEXT);
    }

    /**
     * Removes all components from the inspector keeping the title.
     */
    private void removeComponents() {
        getChildren().remove(1, getChildren().size());
        headerContainer.getChildren().remove(1, headerContainer.getChildren().size());
    }

    private void openComponentSelector() {
        ComponentSelector selector = new ComponentSelector(gameObject, Map.of(
            "Native components", GameComponentTypes.getComponentTypes(), 
            "Scripts", GameComponentTypes.getScriptComponents()
        ));

        selector.setOnComponentSelected(e -> addComponentElement(e.getGameComponent()));
        
        Point2D pos = addComponentButton.localToScreen(0, addComponentButton.getHeight());
        selector.show(addComponentButton, pos.getX(), pos.getY());
    }

    public GameObject getSelectedGameObject() {
        return gameObject;
    }

    private void addComponentElement(GameComponent component) {
        ComponentInspector inspector = new ComponentInspector(component);
        inspector.setContextMenu(new ComponentContextMenu(component, this));
        getChildren().add(getChildren().size() - 1, inspector);
    }

    private void toggleGameObject() {
        if (gameObject != null && onGameObjectToggle != null) {
            gameObject.setDisabled(!gameObject.isDisabled());
            onGameObjectToggle.handle(new GameObjectEvent(GameObjectEvent.GAME_OBJECT_TOGGLED, gameObject));
        }
    }

    public void setOnGameObjectToggle(EventHandler<GameObjectEvent> handler) {
        this.onGameObjectToggle = handler;
    }
}
