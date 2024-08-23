package io.github.kuggek.editor.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.kuggek.editor.GameEngineEditor;
import io.github.kuggek.engine.core.config.EngineProjectConfiguration;
import io.github.kuggek.engine.core.config.ProjectPaths;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class NewProjectController extends Pane {
    @FXML
    private VBox root;

    @FXML
    private TextField projectName;

    @FXML
    private Label locationLabel;
    @FXML
    private Button browseButton;

    @FXML
    private TextArea projectDescription;

    @FXML
    private TextField projectAuthor;

    @FXML
    private Button createProject;

    @FXML
    private void initialize() {
        getChildren().add(root);
        projectName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                createProject.setDisable(true);
            } else {
                createProject.setDisable(false);
            }
        });

        reset();
    }

    public void reset() {
        projectName.clear();
        projectDescription.clear();
        projectAuthor.clear();
    }

    @FXML
    private void createProject() {
        EngineProjectConfiguration configuration = new EngineProjectConfiguration(900, 600, projectName.getText(), false, true, 60);
        configuration.setProjectName(projectName.getText());
        configuration.setProjectDescription(projectDescription.getText());
        configuration.setProjectAuthor(projectAuthor.getText());

        configuration.setProjectVersion("1.0.0-SNAPSHOT");
        configuration.setSceneIDs(new ArrayList<>(List.of("NewScene")));
        configuration.setInitialSceneName("NewScene");

        boolean created = GameEngineEditor.getGameEngineManager().createProject(configuration, locationLabel.getText(), projectName.getText());

        if (created) {
            GameEngineEditor.getGameEngineManager().openProject(
                ProjectPaths.concatenateAndFormat(locationLabel.getText(), projectName.getText())
            );
            getScene().getWindow().hide();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to create project", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void browseLocation() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Project Location");
        directoryChooser.setInitialDirectory(new File("../"));
        File selectedDirectory = directoryChooser.showDialog(getScene().getWindow());
        if (selectedDirectory != null) {
            locationLabel.setText(selectedDirectory.getAbsolutePath());
        } else {
            locationLabel.setText("");
        }
    }
}
