package io.github.kuggek.editor.elements.gameobject.gamecomponent;

import java.lang.reflect.Field;
import java.util.Comparator;

import io.github.kuggek.editor.elements.DropDownElement;
import io.github.kuggek.engine.ecs.GameComponent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

public class ComponentInspector extends DropDownElement<VBox> {
    private GameComponent component;

    public ComponentInspector(GameComponent component) {
        super(component.getClass().getSimpleName());
        
        this.component = component;

        VBox ddVBox = new VBox();
        setDdElement(ddVBox);

        header.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                toggleDropDown();
            }
        });

        for (var field : GameComponent.getComponentFields(this.component.getClass()).stream().sorted(Comparator.comparing(Field::getName)).toList()) {
            ComponentUIField fieldUI = new ComponentUIField(field, this.component);
            ddVBox.getChildren().add(fieldUI);
        }
    }

    public GameComponent getComponent() {
        return component;
    }
}
