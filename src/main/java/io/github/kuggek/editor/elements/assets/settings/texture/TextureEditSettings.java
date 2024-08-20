package io.github.kuggek.editor.elements.assets.settings.texture;

import java.io.File;
import java.io.IOException;

import io.github.kuggek.editor.elements.assets.settings.AssetEditSettings;
import io.github.kuggek.engine.rendering.objects.Texture;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

public class TextureEditSettings extends AssetEditSettings<TextureEditSettings.TextureEvent> {
    
    public class TextureEvent extends Event {
        private static final EventType<TextureEvent> TEXTURE_EVENT = new EventType<>(Event.ANY, "TEXTURE_EVENT");
        
        private Texture texture;
        public TextureEvent(Texture texture) {
            super(TEXTURE_EVENT);
            this.texture = texture;
        }

        public Texture getTexture() {
            return texture;
        }
    }

    private Texture texture;
    private boolean textureChanged = false;

    private final TextField nameField = new TextField();

    private final Label idLabel = new Label();
    private final Label sizeLabel = new Label();
    private final Label formatLabel = new Label();
    private final HBox labelContainer = new HBox();

    private final Button importButton = new Button("Import");

    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final Button cancelButton = new Button("Cancel");
    private final Region spacer = new Region();
    private final HBox buttonContainer = new HBox();

    public TextureEditSettings() {
        labelContainer.getChildren().addAll(sizeLabel, formatLabel);

        buttonContainer.getChildren().addAll(saveButton, deleteButton, cancelButton, spacer);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        importButton.setOnAction(e -> importTexture());

        saveButton.setOnAction(e -> save());
        saveButton.setDisable(true);
        deleteButton.setOnAction(e -> delete());
        deleteButton.setDisable(true);
        cancelButton.setOnAction(e -> close());

        getChildren().addAll(
            idLabel, 
            nameField, 
            labelContainer, 
            importButton, 
            buttonContainer
        );
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        nameField.setText(texture.getName());
        idLabel.setText("ID: " + texture.getID());
        sizeLabel.setText("Size: " + texture.getWidth() + "x" + texture.getHeight());
        formatLabel.setText("Format: " + texture.getFileName().substring(texture.getFileName().lastIndexOf('.') + 1));

        saveButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    @Override
    public void close() {
        if (texture == null) {
            return;
        }

        handleOnClose(new TextureEvent(texture));
    }

    @Override
    public void delete() {
        if (texture == null) {
            return;
        }

        handleOnDelete(new TextureEvent(texture));
    }

    @Override
    public void save() {
        if (texture == null) {
            return;
        }

        if (!textureChanged && nameField.getText().equals(texture.getName())) {
            return;
        }

        texture.setName(nameField.getText());
        handleOnSave(new TextureEvent(texture));
    }

    private void importTexture() {
        FileChooser fileChooser = TextureFileChooser.get();
        
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }

        try {
            int id = texture == null ? -1 : texture.getID();
            texture = Texture.loadTexture(file);
            texture.setID(id);
            setTexture(texture);
            textureChanged = true;
        } catch (IOException e) {
            System.out.println("Failed to import texture: " + e.getMessage());
        }
    }
}
