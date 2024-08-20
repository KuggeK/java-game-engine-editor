package io.github.kuggek.editor.elements.gameobject;

import io.github.kuggek.engine.ecs.GameComponent;
import javafx.event.Event;
import javafx.event.EventType;

public class GameComponentEvent extends Event {

    public static final EventType<GameComponentEvent> GAME_COMPONENT_SELECTED = new EventType<>(Event.ANY, "GAME_COMPONENT_SELECTED");

    private GameComponent gameComponent;

    public GameComponentEvent(EventType<? extends Event> eventType, GameComponent gameComponent) {
        super(eventType);
        this.gameComponent = gameComponent;
    }

    public GameComponent getGameComponent() {
        return gameComponent;
    }
    
}
