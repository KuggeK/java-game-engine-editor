package io.github.kuggek.editor.elements.assets.settings.material;

import org.joml.Vector4f;

import io.github.kuggek.editor.elements.assets.settings.AssetEditSettings;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.joml.Vector4fField;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic.FloatField;
import io.github.kuggek.engine.rendering.objects.Material;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class MaterialEditSettings extends AssetEditSettings<MaterialEditSettings.MaterialEvent> {

    public class MaterialEvent extends Event {
        private static final EventType<MaterialEvent> MATERIAL_EVENT = new EventType<>(Event.ANY, "MATERIAL_EVENT");
        private Material material;

        public MaterialEvent(Material material) {
            super(MATERIAL_EVENT);
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }

    private Material material;

    private final TextField nameField = new TextField();
    
    private final Label idLabel = new Label();

    private final Label ambientLabel = new Label("Ambient:");
    private final Vector4fField ambientField = new Vector4fField();

    private final Label diffuseLabel = new Label("Diffuse:");
    private final Vector4fField diffuseField = new Vector4fField();

    private final Label specularLabel = new Label("Specular:");
    private final Vector4fField specularField = new Vector4fField();

    private final Label shininessLabel = new Label("Shininess:");
    private final FloatField shininessField = new FloatField();

    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final Region spacer = new Region();
    private final Button cancelButton = new Button("Cancel");
    private final HBox buttonContainer = new HBox(spacer, saveButton, deleteButton, cancelButton);

    public MaterialEditSettings() {
        getChildren().addAll(
            nameField,
            idLabel,
            ambientLabel,
            ambientField,
            diffuseLabel,
            diffuseField,
            specularLabel,
            specularField,
            shininessLabel,
            shininessField,
            buttonContainer
        );

        saveButton.setOnAction(e -> save());
        deleteButton.setOnAction(e -> delete());
        cancelButton.setOnAction(e -> close());
        HBox.setHgrow(spacer, Priority.ALWAYS);
    }

    public void setMaterial(Material material) {
        this.material = material;

        nameField.setText(material.getName());
        idLabel.setText("ID: " + material.getID());
        ambientField.setValue(new Vector4f(material.getAmbient()));
        diffuseField.setValue(new Vector4f(material.getDiffuse()));
        specularField.setValue(new Vector4f(material.getSpecular()));
        shininessField.setValue(material.getShininess());
    }
    

    @Override
    public void save() {
        material = new Material(
            material.getID(),
            nameField.getText(),
            ambientField.getValue(),
            diffuseField.getValue(),
            specularField.getValue(),
            shininessField.getValue()
        );
        
        handleOnSave(new MaterialEvent(material));
    }

    @Override
    public void delete() {
        handleOnDelete(new MaterialEvent(material));
    }

    @Override
    public void close() {
        handleOnClose(new MaterialEvent(material));
    }
    

}
