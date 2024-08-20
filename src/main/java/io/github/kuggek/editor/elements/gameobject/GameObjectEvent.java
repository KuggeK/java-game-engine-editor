package io.github.kuggek.editor.elements.gameobject;

import io.github.kuggek.engine.ecs.GameObject;
import javafx.event.Event;
import javafx.event.EventType;

public class GameObjectEvent extends Event {

    public static final EventType<GameObjectEvent> GAME_OBJECT_SELECTED = new EventType<>(Event.ANY, "GAME_OBJECT_SELECTED");
    public static final EventType<GameObjectEvent> GAME_OBJECT_DELETED = new EventType<>(Event.ANY, "GAME_OBJECT_DELETED");
    public static final EventType<GameObjectEvent> GAME_OBJECT_TOGGLED = new EventType<>(Event.ANY, "GAME_OBJECT_TOGGLED");

    private GameObject source;

    public GameObjectEvent(EventType<? extends Event> eventType, GameObject source) {
        super(eventType);
        this.source = source;
    }

    @Override
    public GameObject getSource() {
        return source;
    }
}
