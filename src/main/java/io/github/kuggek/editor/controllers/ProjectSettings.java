package io.github.kuggek.editor.controllers;

import io.github.kuggek.editor.GameEngineEditor;
import io.github.kuggek.editor.elements.gameobject.gamecomponent.fields.basic.IntField;
import io.github.kuggek.engine.core.config.EngineProjectConfiguration;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProjectSettings extends ScrollPane {
    private EngineProjectConfiguration configuration;

    private final VBox root = new VBox();

    private final TextField projName = new TextField();
    private final TextField projDescription = new TextField();
    private final TextField projVersion = new TextField();
    private final TextField projAuthor = new TextField();

    private final VBox scenesContainer = new VBox();
    private final Button addScene = new Button("Add Scene");
    private final TextField initialScene = new TextField();

    private final IntField width = new IntField();
    private final IntField height = new IntField();
    private final TextField title = new TextField();
    private final CheckBox fullscreen = new CheckBox("Fullscreen");
    private final CheckBox resizable = new CheckBox("Resizable");
    private final IntField targetFPS = new IntField();

    private final Button save = new Button("Save");
    private final Button close = new Button("Close");

    public ProjectSettings() {
        configuration = EngineProjectConfiguration.get();
    }

    public void initialize() {
        // TODO label for project path
        projName.setText(configuration.getProjectName());
        projDescription.setText(configuration.getProjectDescription());
        projVersion.setText(configuration.getProjectVersion());
        projAuthor.setText(configuration.getProjectAuthor());
        
        addScene.setOnAction(e -> addScene("New Scene"));
        scenesContainer.getChildren().add(addScene);
        for (String scene : configuration.getScenes()) {
            addScene(scene);
        }
        initialScene.setText(configuration.getInitialSceneName());

        width.setValue(configuration.getWidth());
        height.setValue(configuration.getHeight());
        title.setText(configuration.getTitle());
        fullscreen.setSelected(configuration.isFullscreen());
        resizable.setSelected(configuration.isResizable());
        targetFPS.setValue(configuration.getTargetFPS());

        save.setOnAction(e -> save());
        close.setOnAction(e -> getScene().getWindow().hide());

        root.getChildren().addAll(
                projName, projDescription, projVersion, projAuthor,
                scenesContainer, initialScene,
                width, height, title, fullscreen, resizable, targetFPS,
                save, close
        );

        root.setSpacing(10);
        setContent(root);

        setPrefWidth(500);
        setPrefHeight(500);
    }

    private void addScene(String scene) {
        HBox sceneBox = new HBox();
        sceneBox.getChildren().add(new TextField(scene));
        Button remove = new Button("Remove");
        remove.setOnAction(e -> scenesContainer.getChildren().remove(sceneBox));
        sceneBox.getChildren().add(remove);
        scenesContainer.getChildren().add(scenesContainer.getChildren().size() - 1, sceneBox);
    }

    public void save() {
        configuration.setProjectName(projName.getText());
        configuration.setProjectDescription(projDescription.getText());
        configuration.setProjectVersion(projVersion.getText());
        configuration.setProjectAuthor(projAuthor.getText());

        configuration.setInitialSceneName(initialScene.getText());
        configuration.getScenes().clear();
        for (int i = 0; i < scenesContainer.getChildren().size() - 1; i++) {
            HBox sceneBox = (HBox) scenesContainer.getChildren().get(i);
            TextField scene = (TextField) sceneBox.getChildren().get(0);
            configuration.getScenes().add(scene.getText());
        } 

        configuration.setWidth(width.getValue());
        configuration.setHeight(height.getValue());
        configuration.setTitle(title.getText());
        configuration.setFullscreen(fullscreen.isSelected());
        configuration.setResizable(resizable.isSelected());
        configuration.setTargetFPS(targetFPS.getValue());

        GameEngineEditor.getGameEngineManager().saveCurrentProject();
    }
}
