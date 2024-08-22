package io.github.kuggek.editor.controllers;

import java.io.File;

import io.github.kuggek.editor.GameEngineEditor;
import io.github.kuggek.editor.GameEngineManager;
import io.github.kuggek.editor.elements.gameobject.GameComponentTypes;
import io.github.kuggek.engine.core.config.EngineProjectConfiguration;
import io.github.kuggek.engine.scripting.ScriptLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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


    private Alert saveConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save the current scene?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

    public EditorMenuBar() {
        super();
        ScriptLoader.compileAndPackageScripts("scripts.jar", EngineProjectConfiguration.get().getPaths().getScriptsPath());
        ScriptLoader.addJarToClasspath("scripts.jar");
        GameComponentTypes.addScripts();
    }

    @FXML
    public void initialize() {
        GameEngineManager gameEngineManager = GameEngineEditor.getGameEngineManager();

        newMI.setOnAction(e -> System.out.println("New"));
        
        openMI.setOnAction(e ->  {
            FileChooser projectChooser = new FileChooser();
            projectChooser.setTitle("Open Project");
            projectChooser.setInitialDirectory(new File("."));
            projectChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project Files", "*.json"));
            File project = projectChooser.showOpenDialog(null);
            if (project != null) {
                gameEngineManager.openProject(project.getAbsolutePath());
            }
        });

        saveMI.setOnAction(e -> gameEngineManager.saveCurrentScene());

        projectSettingsMI.setOnAction(e -> System.out.println("Project Settings"));

        EngineProjectConfiguration.get().getScenes().forEach(scene -> {
            MenuItem item = new MenuItem(scene);
            item.setOnAction(e -> gameEngineManager.loadScene(scene));
            changeSceneMI.getItems().add(item);
        });

        compileScriptsMI.setOnAction(e -> {
            ScriptLoader.compileAndPackageScripts("scripts.jar", EngineProjectConfiguration.get().getPaths().getScriptsPath());
            ScriptLoader.addJarToClasspath("scripts.jar");
            GameComponentTypes.addScripts();
        });

        preferencesMI.setOnAction(e -> System.out.println("Preferences"));

        update();
    }

    public void update() {
        changeSceneMI.getItems().clear();
        GameEngineManager gameEngineManager = GameEngineEditor.getGameEngineManager();
        EngineProjectConfiguration.get().getScenes().forEach(scene -> {
            MenuItem item = new MenuItem(scene);
            item.setOnAction(e -> {
                saveConfirmation.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.CANCEL) {
                        return;
                    }
                    if (response == ButtonType.YES) {
                        gameEngineManager.saveCurrentScene();
                    }
                    gameEngineManager.loadScene(scene);
                });
            });
            changeSceneMI.getItems().add(item);
        });
    }
}
