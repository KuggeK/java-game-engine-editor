package io.github.kuggek.editor.controllers;

import java.io.File;
import java.net.URL;

import io.github.kuggek.editor.GameEngineEditor;
import io.github.kuggek.editor.GameEngineManager;
import io.github.kuggek.editor.elements.gameobject.GameComponentTypes;
import io.github.kuggek.engine.core.config.EngineProjectConfiguration;
import io.github.kuggek.engine.core.config.ProjectPaths;
import io.github.kuggek.engine.ecs.GameScene;
import io.github.kuggek.engine.scripting.ScriptLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class EditorMenuBar extends MenuBar {
    
    @FXML
    private MenuBar menuBar; 

    @FXML 
    private Menu projectMenu;
        @FXML private MenuItem newProjectMI;
        @FXML private MenuItem openProjectMI;
        @FXML private MenuItem projectSettingsMI;
        @FXML private Menu scriptsMenu;
            @FXML private MenuItem compileScriptsMI;

    @FXML
    private Menu sceneMenu;
        @FXML private MenuItem saveSceneMI;
        @FXML private Menu changeSceneMI;    

    private Alert saveConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save the current scene?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

    private NewProjectController newProjectController;
    private Stage newProjectStage;
    private Scene newProjectScene;

    public EditorMenuBar() {
        super();
        ScriptLoader.compileAndPackageScripts("scripts.jar", ProjectPaths.SCRIPTS_PATH);
        ScriptLoader.addJarToClasspath("scripts.jar");
        GameComponentTypes.addScripts();
    }

    @FXML
    public void initialize() {
        GameEngineManager gameEngineManager = GameEngineEditor.getGameEngineManager();

        newProjectMI.setOnAction(e -> {
            if (newProjectController == null) {
                loadNewProjectSettings();
            }
            
            newProjectController.reset();

            if (newProjectScene == null) {
                newProjectScene = new Scene(newProjectController);
                newProjectStage = new Stage();
                newProjectStage.setScene(newProjectScene);
                newProjectStage.setTitle("New Project");
            }
            newProjectStage.show();
        });
        
        openProjectMI.setOnAction(e ->  {
            DirectoryChooser projectChooser = new DirectoryChooser();
            projectChooser.setTitle("Open Project");
            projectChooser.setInitialDirectory(new File("../"));
            File project = projectChooser.showDialog(null);
            if (project != null) {
                gameEngineManager.openProject(project.getAbsolutePath());
            } 
        });

        saveSceneMI.setOnAction(e -> {
            if (!gameEngineManager.saveCurrentScene()) {
                new Alert(Alert.AlertType.ERROR, "Failed to save scene").show();
            }
        });

        projectSettingsMI.setOnAction(e -> {
            Stage stage = new Stage();
            ProjectSettings projectSettings = new ProjectSettings();
            projectSettings.initialize();
            stage.setScene(new Scene(projectSettings));
            stage.show();
        });

        EngineProjectConfiguration.get().getScenes().forEach(scene -> {
            MenuItem item = new MenuItem(scene);
            item.setOnAction(e -> gameEngineManager.loadScene(scene));
            changeSceneMI.getItems().add(item);
        });

        compileScriptsMI.setOnAction(e -> {
            ScriptLoader.compileAndPackageScripts("scripts.jar", ProjectPaths.SCRIPTS_PATH);
            ScriptLoader.addJarToClasspath("scripts.jar");
            GameComponentTypes.addScripts();
        });

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
                    
                    GameScene newScene = gameEngineManager.loadScene(scene);
                    if (newScene == null) {
                        new Alert(Alert.AlertType.ERROR, "Failed to load scene").show();
                    }
                });
            });
            changeSceneMI.getItems().add(item);
        });
    }

    private void loadNewProjectSettings() {
        URL location = getClass().getResource("/fxml/newProject.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        try {
            loader.load();
            newProjectController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
