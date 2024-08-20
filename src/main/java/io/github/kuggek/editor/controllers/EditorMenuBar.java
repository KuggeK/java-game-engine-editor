package io.github.kuggek.editor.controllers;

import java.io.File;

import io.github.kuggek.editor.elements.gameobject.GameComponentTypes;
import io.github.kuggek.engine.core.config.EngineProjectConfiguration;
import io.github.kuggek.engine.scripting.ScriptLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

public class EditorMenuBar extends MenuBar {
    
    @FXML
    private MenuBar menuBar; 

    @FXML 
    private Menu projectMenu;
        @FXML private MenuItem newMI;
        @FXML private MenuItem openMI;
        @FXML private MenuItem saveMI;
        @FXML private MenuItem projectSettingsMI;
        @FXML private Menu changeSceneMI;

    @FXML
    private Menu editMenu;
        @FXML private Menu scriptsMenu;
            @FXML private MenuItem compileScriptsMI;
        @FXML private MenuItem preferencesMI;

    private EventHandler<ActionEvent> onSaveAction;
    private EventHandler<ActionEvent> onChangeScene;
    private EventHandler<ActionEvent> onOpenProject;

    public EditorMenuBar() {
        super();
        ScriptLoader.compileAndPackageScripts("scripts.jar", EngineProjectConfiguration.get().getPaths().getScriptsPath());
        ScriptLoader.addJarToClasspath("scripts.jar");
        GameComponentTypes.addScripts();
    }

    @FXML
    public void initialize() {
        newMI.setOnAction(e -> System.out.println("New"));
        
        openMI.setOnAction(e ->  {
            FileChooser projectChooser = new FileChooser();
            projectChooser.setTitle("Open Project");
            projectChooser.setInitialDirectory(new File("."));
            projectChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project Files", "*.json"));
            File project = projectChooser.showOpenDialog(null);
            if (project != null && onOpenProject != null) {
                onOpenProject.handle(new ActionEvent(project, openMI));
            }
        });

        saveMI.setOnAction(e -> {
            if (onSaveAction != null) onSaveAction.handle(e);
        });
        projectSettingsMI.setOnAction(e -> System.out.println("Project Settings"));

        EngineProjectConfiguration.get().getScenes().forEach(scene -> {
            MenuItem item = new MenuItem(scene);
            item.setOnAction(event -> {
                if (onChangeScene != null) onChangeScene.handle(new ActionEvent(scene, item));
            });
            changeSceneMI.getItems().add(item);
        });

        compileScriptsMI.setOnAction(e -> {
            ScriptLoader.compileAndPackageScripts("scripts.jar", EngineProjectConfiguration.get().getPaths().getScriptsPath());
            ScriptLoader.addJarToClasspath("scripts.jar");
            GameComponentTypes.addScripts();
        });
        preferencesMI.setOnAction(e -> System.out.println("Preferences"));


    }

    public void setOnSaveAction(EventHandler<ActionEvent> onSaveAction) {
        this.onSaveAction = onSaveAction;
    }

    public void setOnChangeScene(EventHandler<ActionEvent> onChangeScene) {
        this.onChangeScene = onChangeScene;
    }

    public void setOnOpenProject(EventHandler<ActionEvent> onOpenProject) {
        this.onOpenProject = onOpenProject;
    }

    public void update() {
        changeSceneMI.getItems().clear();
        EngineProjectConfiguration.get().getScenes().forEach(scene -> {
            MenuItem item = new MenuItem(scene);
            item.setOnAction(event -> {
                if (onChangeScene != null) onChangeScene.handle(new ActionEvent(scene, item));
            });
            changeSceneMI.getItems().add(item);
        });
    }
}
