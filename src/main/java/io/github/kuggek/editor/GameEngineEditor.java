package io.github.kuggek.editor;

import java.io.IOException;

import io.github.kuggek.editor.controllers.MainLayout;
import io.github.kuggek.editor.elements.GLPanel;
import io.github.kuggek.engine.GameEngine;
import io.github.kuggek.engine.core.assets.AssetManager;
import io.github.kuggek.engine.core.assets.SQLiteAssetManager;
import io.github.kuggek.engine.core.assets.DefaultAssets;
import io.github.kuggek.engine.core.config.EngineProjectConfiguration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameEngineEditor extends Application {
    
    private EngineProjectConfiguration configuration;
    private MainLayout mainLayout;
    private GLPanel glPanel;
    private GameEngine engine;
    private Scene mainScene;

    @Override
    public void init() throws Exception {
        configuration = EngineProjectConfiguration.loadProjectConfiguration("defaultProject.json");
        AssetManager assetManager = SQLiteAssetManager.getInstance();
        DefaultAssets.loadDefaultAssets(assetManager);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(configuration.getTitle());

        glPanel = new GLPanel();

        engine = new GameEngine();
        engine.initialize(configuration, glPanel);

        mainLayout = loadMainLayout();
        mainLayout.linkToEngine(engine);
        mainLayout.setGameViewContent(glPanel);

        mainScene = new Scene(mainLayout.getRoot());
        stage.setScene(mainScene);

        engine.getSubsystems().getRenderingEngine().render(true);

        stage.setOnCloseRequest(e -> {
            engine.stopGameLoop();
            glPanel.destroy();
            mainLayout.stop();
        });

        stage.setFullScreen(configuration.isFullscreen());
        stage.setResizable(configuration.isResizable());

        stage.show();
    }


    private MainLayout loadMainLayout() {
        FXMLLoader loader = new FXMLLoader(GameEngineEditor.class.getResource("/fxml/main.fxml"));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
