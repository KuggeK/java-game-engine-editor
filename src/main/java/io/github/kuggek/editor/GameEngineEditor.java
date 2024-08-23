package io.github.kuggek.editor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.kuggek.editor.controllers.MainLayout;
import io.github.kuggek.editor.elements.GLPanel;
import io.github.kuggek.engine.GameEngine;
import io.github.kuggek.engine.core.assets.AssetManager;
import io.github.kuggek.engine.core.assets.SQLiteAssetManager;
import io.github.kuggek.engine.core.assets.DefaultAssets;
import io.github.kuggek.engine.core.assets.ResourceManager;
import io.github.kuggek.engine.core.config.EngineProjectConfiguration;
import io.github.kuggek.engine.core.config.ProjectPaths;
import io.github.kuggek.engine.core.json.GameSceneAdapters;
import io.github.kuggek.engine.ecs.GameScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameEngineEditor extends Application implements GameEngineManager {
    
    private EngineProjectConfiguration configuration;
    private MainLayout mainLayout;
    private GLPanel glPanel;
    private GameEngine engine;
    private Scene mainScene;
    private Gson gson;

    @Override
    public void init() throws Exception {
        configuration = EngineProjectConfiguration.loadProjectConfiguration("./");
        AssetManager assetManager = SQLiteAssetManager.getInstance();
        DefaultAssets.loadDefaultAssets(assetManager);
        setGameEngineManager(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(configuration.getTitle());

        glPanel = new GLPanel();

        engine = new GameEngine();
        engine.initialize(configuration, glPanel);

        // Initialize gson
        GsonBuilder builder = new GsonBuilder();
        GameSceneAdapters.registerAdapters(builder);
        gson = builder.create();

        // Load main layout
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

    @Override
    public GameEngine initializeEngine() {
        return engine;
    }

    @Override
    public GameScene loadScene(String sceneName) {
        String path = getScenePath(sceneName);
        try {
            String json = Files.readString(Paths.get(path));
            GameScene newScene = gson.fromJson(json, GameScene.class);
            engine.setupScene(newScene);
            mainLayout.setupScene();
            return newScene;
        } catch (IOException e1) {
            System.out.println(e1.getMessage());
            return null;
        }
    }

    @Override
    public boolean saveCurrentScene() {
        String json = gson.toJson(engine.getCurrentScene());
        String path = getScenePath(engine.getCurrentScene().getName());
        try {
            Files.write(Paths.get(path), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e1) {
            System.out.println(e1.getMessage());
            return false;
        }
    }

    @Override
    public boolean saveCurrentSceneAs(String sceneName) {
        engine.getCurrentScene().setName(sceneName);
        return saveCurrentScene();
    }

    private String getScenePath(String sceneName) {
        return ProjectPaths.concatenateAndFormat(
            configuration.getProjectAbsolutePath(),
            ProjectPaths.SCENES_PATH, 
            sceneName + ".json"
        );
    }

    @Override
    public GameEngine openProject(String projectPath) {
        try {
            EngineProjectConfiguration project = EngineProjectConfiguration.loadProjectConfiguration(projectPath);
            return openProject(project);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    private GameEngine openProject(EngineProjectConfiguration project) throws IOException {
        configuration = project;
        EngineProjectConfiguration.set(project);
        engine.initialize(configuration, glPanel);
        mainLayout.setupScene();
        return engine;
    }

    @Override
    public boolean saveCurrentProject() {
        try {
            String json = gson.toJson(configuration);
            String path = ProjectPaths.concatenateAndFormat(
                configuration.getProjectAbsolutePath(),
                ProjectPaths.PROJECT_CONFIG_PATH
            );
            Files.write(Paths.get(path), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createProject(EngineProjectConfiguration configuration, String projectParentPath, String projectFolderName) {
        try {
            // Create project directories
            String projectPath = ProjectPaths.concatenateAndFormat(projectParentPath, projectFolderName);
            new File(projectPath).mkdirs();
            new File(ProjectPaths.concatenateAndFormat(projectPath, ProjectPaths.SCENES_PATH)).mkdirs();
            new File(ProjectPaths.concatenateAndFormat(projectPath, ProjectPaths.SCRIPT_DEPENDENCIES_PATH)).mkdirs();
            new File(ProjectPaths.concatenateAndFormat(projectPath, ".assets")).mkdirs();

            String json = gson.toJson(configuration);
            String path = ProjectPaths.concatenateAndFormat(
                projectPath,
                ProjectPaths.PROJECT_CONFIG_PATH
            );

            Files.write(Paths.get(path), json.getBytes(), StandardOpenOption.CREATE_NEW);

            String initialScene = ResourceManager.readFile("/default/newSceneTemplate.json", GameEngineEditor.class);
            System.out.println("Scene: " + initialScene);
            String scenePath = ProjectPaths.concatenateAndFormat(
                projectPath,
                ProjectPaths.SCENES_PATH,
                "NewScene.json"
            );
            Files.write(Paths.get(scenePath), initialScene.getBytes(), StandardOpenOption.CREATE_NEW);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Static stuff
    private static GameEngineEditor instance;
    public static GameEngineManager getGameEngineManager() {
        return instance;
    }

    private static void setGameEngineManager(GameEngineEditor instance) {
        GameEngineEditor.instance = instance;
    }
}
