package io.github.kuggek.editor.elements.assets.settings.mesh;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import io.github.kuggek.editor.elements.assets.settings.AssetEditSettings;
import io.github.kuggek.engine.rendering.objects.Mesh;
import io.github.kuggek.engine.rendering.objects.Meshes;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

public class MeshEditSettings extends AssetEditSettings<MeshEditSettings.MeshEvent> {

    public class MeshEvent extends Event {
        private static final EventType<MeshEvent> MESH_EVENT = new EventType<>(Event.ANY, "MESH_EVENT");
        
        private Mesh mesh;
        public MeshEvent(Mesh mesh) {
            super(MESH_EVENT);
            this.mesh = mesh;
        }

        public Mesh getMesh() {
            return mesh;
        }
    }

    private final Label idLabel = new Label();
    private final TextField nameField = new TextField();

    private final Label vertexCountLabel = new Label();
    private final Label faceCountLabel = new Label();
    private final HBox labelContainer = new HBox();

    private final Button importButton = new Button("Import Mesh");

    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final Button cancelButton = new Button("Cancel");
    private final Region spacer = new Region();
    private final HBox buttonContainer = new HBox();

    private Mesh mesh;

    private boolean meshChanged = false;

    public MeshEditSettings() {
        labelContainer.getChildren().addAll(vertexCountLabel, faceCountLabel);

        importButton.setOnAction(e -> openImportDialog());
        saveButton.setOnAction(e -> save());
        deleteButton.setOnAction(e -> delete());

        cancelButton.setOnAction(e -> close());

        buttonContainer.getChildren().addAll(saveButton, deleteButton, spacer, cancelButton);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(
                idLabel,
                nameField,
                labelContainer,
                importButton,
                buttonContainer
        );
    }

    public MeshEditSettings(Mesh mesh) {
        this();
        setAsset(mesh);
    }

    public void setAsset(Mesh mesh) {
        this.mesh = mesh;
        updateLabels();
    }

    @Override
    public void save() {
        // Return if no changes were made
        if (!meshChanged && nameField.getText().equals(mesh.getName())) {
            return;
        }
        
        mesh.setName(nameField.getText());
        handleOnSave(new MeshEvent(mesh));
    }

    @Override
    public void delete() {
        handleOnDelete(new MeshEvent(mesh));
    }

    @Override
    public void close() {
        handleOnClose(new MeshEvent(mesh));
    }
    
    private void openImportDialog() {
        FileChooser fileChooser = MeshFileChooser.get();
        File file = fileChooser.showOpenDialog(null);

        if (file == null) {
            return;
        }

        try {
            int id = mesh == null ? -1 : mesh.getID();
            mesh = Meshes.loadMesh(file);
            mesh.setID(id);
            updateLabels();
            meshChanged = true;
        } catch (IOException | URISyntaxException e) {
            System.out.println("Failed to import mesh: " + e.getMessage());
        } 
    }

    private void updateLabels() {
        if (mesh == null) {
            idLabel.setText("ID: ");
            nameField.setText("");
            vertexCountLabel.setText("Vertex count: ");
            faceCountLabel.setText("Face count: ");
            return;
        }

        idLabel.setText("ID: " + mesh.getID());
        nameField.setText(mesh.getName());
        vertexCountLabel.setText("Vertex count: " + mesh.getNumVertices());
        faceCountLabel.setText("Face count: " + mesh.getNumIndices() / 3);
    }

    public Mesh getAsset() {
        return mesh;
    }
}
