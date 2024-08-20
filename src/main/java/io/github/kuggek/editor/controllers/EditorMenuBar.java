package io.github.kuggek.editor.controllers;

import io.github.kuggek.editor.elements.gameobject.GameComponentTypes;
import io.github.kuggek.engine.core.config.EngineProjectConfiguration;
import io.github.kuggek.engine.scripting.ScriptLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class EditorMenuBar extends MenuBar {
    
    @FXML
    private MenuBar menuBar; 

    @FXML 
    private Menu fileMenu;
        @FXML private MenuItem newMI;
        @FXML private MenuItem openMI;
        @FXML private MenuItem saveMI;
        @FXML private MenuItem projectSettingsMI;

    @FXML
    private Menu editMenu;
        @FXML private Menu scriptsMenu;
            @FXML private MenuItem compileScriptsMI;
        @FXML private MenuItem preferencesMI;

    private EventHandler<ActionEvent> onSaveAction;

    public EditorMenuBar() {
        super();
        ScriptLoader.compileAndPackageScripts("scripts.jar", EngineProjectConfiguration.get().getPaths().getScriptsPath());
        ScriptLoader.addJarToClasspath("scripts.jar");
        GameComponentTypes.addScripts();
    }

    @FXML
    public void initialize() {
        newMI.setOnAction(e -> System.out.println("New"));
        openMI.setOnAction(e -> System.out.println("Open"));
        saveMI.setOnAction(e -> {
            if (onSaveAction != null) onSaveAction.handle(e);
        });
        projectSettingsMI.setOnAction(e -> System.out.println("Project Settings"));

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
}
