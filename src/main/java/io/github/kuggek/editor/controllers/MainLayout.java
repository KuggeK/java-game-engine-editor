package io.github.kuggek.editor.controllers;

import static java.awt.event.KeyEvent.VK_1;

import javax.swing.SwingUtilities;

import io.github.kuggek.editor.elements.GLPanel;
import io.github.kuggek.editor.elements.assets.AssetInspector;
import io.github.kuggek.editor.elements.gameobject.GameObjectInspector;
import io.github.kuggek.editor.elements.gamescene.GameSceneInspector;
import io.github.kuggek.editor.misc.EditorCamera;
import io.github.kuggek.engine.GameEngine;
import io.github.kuggek.engine.ecs.GameObject;
import io.github.kuggek.engine.ecs.GameScene;
import io.github.kuggek.engine.rendering.objects.Camera;
import io.github.kuggek.engine.subsystems.EngineSubsystems;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

public class MainLayout {

    @FXML 
    private VBox root;

    @FXML
    private EditorMenuBar menuBarController;

    @FXML
    private SwingNode gameViewContainer;

    @FXML
    private ToggleButton playButton;
    @FXML
    private Button cameraButton;

    @FXML
    private VBox gameSceneInspectorContainer;
    private GameSceneInspector gameSceneInspector;

    @FXML
    private ScrollPane gameObjectInspectorContainer;
    private GameObjectInspector gameObjectInspector;

    @FXML
    private ScrollPane assetInspectorContainer;
    private AssetInspector assetInspector;

    private GameEngine engine;

    private EditorCamera editorCamera;
    private Camera gameCamera;
    private Camera currentCamera;

    private GameScene sceneBeforePlay;

    // Continuous rendering variables and render loop.
    private boolean render;
    private Runnable renderTask = () -> {
        EngineSubsystems subsystems = engine.getSubsystems();

        long previousTime = System.nanoTime();
        long currentTime;
        float deltaTime;

        render = true;
        while (render) {
            currentTime = System.nanoTime();
            deltaTime = (currentTime - previousTime) / 1e9f;
            previousTime = currentTime;

            editorCamera.update(deltaTime, engine.getWindow().getKeyInput());

            subsystems.getRenderingEngine().render();

            if (engine.getWindow().getKeyInput().isKeyPressed(VK_1)) {
                System.exit(0); 
            }

            // Throttle the render loop 
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    };
    private Thread renderThread;

    @FXML
    public void initialize() {
        // Editor Camera
        editorCamera = new EditorCamera();
        editorCamera.getTransform().setPosition(0, 0, 10);

        // Game Object Inspector
        gameObjectInspector = new GameObjectInspector();
        gameObjectInspectorContainer.setContent(gameObjectInspector);

        gameObjectInspector.setOnGameObjectToggle(e -> {
            gameSceneInspector.reload();
        });

        // Asset Inspector
        assetInspector = new AssetInspector();
        assetInspectorContainer.setContent(assetInspector);
        assetInspectorContainer.setFitToWidth(true);

        // Game Scene Inspector
        gameSceneInspector = new GameSceneInspector(gameSceneInspectorContainer);
        gameSceneInspector.setOnGameObjectSelected(e -> {
            GameObject go = (GameObject) e.getSource();
            gameObjectInspector.selectGameObject(go);
        });
        gameSceneInspector.setOnGameObjectDeleted(e -> {
            if (gameObjectInspector.getSelectedGameObject() == e.getSource()) {
                gameObjectInspector.setToEmpty();
            }
        });
        gameSceneInspector.setOnGameObjectToggled(e -> {
            if (gameObjectInspector.getSelectedGameObject() == e.getSource()) {
                gameObjectInspector.selectGameObject((GameObject) e.getSource());
            }
        });
    }

    public void setGameViewContent(GLPanel glPanel) {
        System.out.println("Setting game view content");
        SwingUtilities.invokeLater(() -> {
            gameViewContainer.setContent(glPanel);
        });
    }

    public VBox getRoot() {
        return root;
    }

    public void linkToEngine(GameEngine engine) {
        this.engine = engine;
        setupScene();

        render = true;
        renderThread = new Thread(renderTask);
        renderThread.start();

        playButton.setOnAction(e -> {
            if (playButton.isSelected() && !engine.isRunning()) {
                render = false;
                try {
                    renderThread.join();
                } catch (InterruptedException e1) {
                    System.out.println(e1.getMessage());
                }

                engine.setupScene(engine.getCurrentScene());
                switchToGameCamera();
                sceneBeforePlay = GameScene.copyScene(engine.getCurrentScene());

                engine.startGameLoop();
                gameViewContainer.requestFocus();

            } else {
                engine.stopGameLoop();
                engine.setupScene(sceneBeforePlay);
                setupScene();

                renderThread = new Thread(renderTask);
                renderThread.start();
            }
        });
    }

    public void setupScene() {
        GameScene currentScene = engine.getCurrentScene();
        if (currentScene == null) {
            return;
        }
        gameSceneInspector.setNewScene(currentScene);
        gameSceneInspector.setGameObjectManager(engine.getSubsystems());

        gameObjectInspector.setToEmpty();

        gameCamera = engine.getSubsystems().getRenderingSettings().getActiveCamera();
        switchToEditorCamera();
    }

    public void selectGameObject(GameObject go) {
        System.out.println("Selected game object with ID " + go.getID());
        
    }

    public void stop() {
        if (renderThread != null && renderThread.isAlive()) {
            System.out.println("Stopping render thread");
            render = false;
            try {
                renderThread.join();
            } catch (InterruptedException e) {
                System.out.println("Hmmm");
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    public void switchCamera() {
        if (currentCamera == editorCamera) {
            switchToGameCamera();
        } else {
            switchToEditorCamera();
        }
    }

    private void switchToEditorCamera() {
        engine.getSubsystems().getRenderingSettings().setActiveCamera(editorCamera);
        currentCamera = editorCamera;
        cameraButton.setText("Switch to Game Camera");
    }

    private void switchToGameCamera() {
        engine.getSubsystems().getRenderingSettings().setActiveCamera(gameCamera);
        currentCamera = gameCamera;
        cameraButton.setText("Switch to Editor Camera");
    }
}
