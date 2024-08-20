package io.github.kuggek.editor.elements.gameobject;

import java.util.List;
import java.util.Map;

import io.github.kuggek.engine.ecs.GameComponent;
import io.github.kuggek.engine.ecs.GameObject;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * A context menu that allows the user to select a component to add to a game object.
 * The context menu is populated with all available components that can be added to the game object.
 */
public class ComponentSelector extends ContextMenu {

    private EventHandler<GameComponentEvent> onComponentSelected;

    /**
     * Creates a new context menu with all available components that can be added to the given game object.
     * @param gameObject 
     * @param componentTypes A double map with the first key being the category name and the second key being
     * the component name. The value is the class of the component. The category name is used to 
     * group the components in the context menu.
     */
    public ComponentSelector(GameObject gameObject, Map<String, Map<String, Class<? extends GameComponent>>> componentTypes) {
        super();

        for (var entry : componentTypes.entrySet()) {
            String category = entry.getKey(); 
            addComponents(gameObject, entry.getValue(), category);
        }
    }

    private void addComponents(GameObject gameObject, Map<String, Class<? extends GameComponent>> components, String category) {
        List<Class<? extends GameComponent>> availableTypes = components.values().stream()
            .filter(clazz -> !gameObject.hasComponentOfType(clazz))
            .toList(); 

        if (availableTypes.isEmpty()) {
            return;
        }

        Menu categoryMenu = new Menu(category);
        getItems().add(categoryMenu);

        for (var componentType : availableTypes) {
            MenuItem item = new MenuItem(componentType.getSimpleName());
            item.setOnAction(e -> {
                try {
                    GameComponent gc = componentType.getConstructor().newInstance();
                    if (onComponentSelected != null && gc != null) {
                        gameObject.addComponent(gc, true);
                        onComponentSelected.handle(new GameComponentEvent(GameComponentEvent.GAME_COMPONENT_SELECTED, gc));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getClass().getName() + ": Failed to create component of type " + componentType.getName() + ". Error message: " + ex.getMessage());
                }
            });            
            categoryMenu.getItems().add(item);
        }
    }

    public void setOnComponentSelected(EventHandler<GameComponentEvent> onComponentSelected) {
        this.onComponentSelected = onComponentSelected;
    }
}
