package io.github.kuggek.editor.elements.gamescene;

import java.util.HashSet;
import java.util.Set;

import io.github.kuggek.editor.elements.gameobject.GameObjectEvent;
import io.github.kuggek.engine.ecs.GameObject;
import io.github.kuggek.engine.ecs.GameObjectManager;
import io.github.kuggek.engine.ecs.GameScene;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class GameSceneInspector {
    private GameScene scene;
    private GameObjectManager gameObjectManager;

    private Pane container;
    private Button addGameObjectButton;

    private EventHandler<GameObjectEvent> onGameObjectSelected;
    private EventHandler<GameObjectEvent> onGameObjectDeleted;
    private EventHandler<GameObjectEvent> onGameObjectToggled;

    private Set<GameObjectDropDown> gameObjectDropDowns;

    public GameSceneInspector(Pane container) {
        this.container = container;

        // Add button to add a new game object
        addGameObjectButton = new Button("Add Game Object");
        addGameObjectButton.setOnAction(e -> addGameObject());  
        container.getChildren().add(addGameObjectButton);

        // Hide the button by default until a manager is set
        addGameObjectButton.setVisible(false);

        gameObjectDropDowns = new HashSet<>();
    }

    public GameSceneInspector(Pane container, GameObjectManager manager, GameScene scene) {
        this(container);
        gameObjectManager = manager;
        addGameObjectButton.setVisible(true);
        setNewScene(scene);
    }

    public void createParentLink(GameObjectDropDown parent, GameObjectDropDown child) {
        GameObject.link(parent.getGameObject(), child.getGameObject());
        parent.addChild(child);
    }

    public void setNewScene(GameScene scene) {
        this.scene = scene;

        // Clear the game scene inspector
        container.getChildren().remove(1, container.getChildren().size());

        // Populate scene inspector with game objects. Choose only root objects because
        // GameObjectDropDown will recursively add children as their own GameObjectDropDowns
        this.scene.getGameObjects().stream()
            .filter(go -> go.getParent() == null)
            .forEach(go -> {
                addGameObject(go);
            });
    }

    public void reload() {
        setNewScene(scene);
    }

    /**
     * Add a game object to the scene inspector container. This game object should already be
     * added to the scene.
     * @param go The game object to add
     */
    private GameObjectDropDown addGameObject(GameObject go) {
        GameObjectDropDown goDD = createGODropDown(go);
        container.getChildren().add(goDD);
        gameObjectDropDowns.add(goDD);
        return goDD;
    }

    private GameObjectDropDown createGODropDown(GameObject go) {
        GameObjectDropDown goDD = new GameObjectDropDown(go, true);

        // Set the event handler for when a game object is selected
        goDD.setOnGameObjectSelected(true, e -> {
            if (onGameObjectSelected != null) {
                onGameObjectSelected.handle(e);
            }
        });

        goDD.setContextMenu(new GameObjectContextMenu(goDD, this));

        return goDD;
    }

    public void removeGameObject(GameObjectDropDown goDD) {
        container.getChildren().remove(goDD);
        goDD.getGameObject().destroy();
        if (onGameObjectDeleted != null) {
            onGameObjectDeleted.handle(new GameObjectEvent(GameObjectEvent.GAME_OBJECT_DELETED, goDD.getGameObject()));
        }
    }

    /**
     * Create and add a new game object to the scene inspector container.
     * @return The GameObjectDropDown element that was created
     */
    public GameObjectDropDown addGameObject() {
        GameObject go = new GameObject(getNextGameObjectID());
        gameObjectManager.addGameObject(go);
        return addGameObject(go);
    }

    public void toggleGameObject(GameObjectDropDown goDD) {
        goDD.getGameObject().setDisabled(!goDD.getGameObject().isDisabled());
        if (onGameObjectToggled != null) {
            onGameObjectToggled.handle(new GameObjectEvent(GameObjectEvent.GAME_OBJECT_TOGGLED, goDD.getGameObject()));
        }
    }

    private int getNextGameObjectID() {
        return scene.getGameObjects().parallelStream()
            .mapToInt(GameObject::getID)
            .max()
            .orElse(0) + 1;
    }

    public void setOnGameObjectSelected(EventHandler<GameObjectEvent> handler) {
        this.onGameObjectSelected = handler;
    }

    public void setOnGameObjectDeleted(EventHandler<GameObjectEvent> handler) {
        this.onGameObjectDeleted = handler;
    }

    public void setOnGameObjectToggled(EventHandler<GameObjectEvent> handler) {
        this.onGameObjectToggled = handler;
    }

    public void setGameObjectManager(GameObjectManager gameObjectManager) {
        this.gameObjectManager = gameObjectManager;
        addGameObjectButton.setVisible(true);
    }
}
