package io.github.kuggek.editor.elements.gameobject.gamecomponent;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import io.github.kuggek.editor.elements.DropDownElement;
import io.github.kuggek.engine.ecs.GameComponent;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

public class ComponentInspector extends DropDownElement<VBox> {
    private GameComponent component;

    private final CheckBox active;

    private final Set<ComponentUIField> fields;

    public ComponentInspector(GameComponent component) {
        super(component.getClass().getSimpleName());
        
        this.component = component;

        VBox ddVBox = new VBox();
        setDdElement(ddVBox);

        fields = new HashSet<>();

        active = new CheckBox();
        active.setSelected(!component.isDisabled());
        active.setOnAction(e -> {
            component.setDisabled(!active.isSelected());
            for (var field : fields) {
                if (component.isDisabled()) {
                    field.disable();
                } else {
                    field.enable();
                }
            }
        });
        header.getChildren().add(1, active);

        header.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                toggleDropDown();
            }
        });

        for (var field : GameComponent.getComponentFields(this.component.getClass()).stream().sorted(Comparator.comparing(Field::getName)).toList()) {
            ComponentUIField fieldUI = new ComponentUIField(field, this.component);
            fields.add(fieldUI);
            ddVBox.getChildren().add(fieldUI);
        }
    }

    public GameComponent getComponent() {
        return component;
    }
}
