package io.github.kuggek.editor;

import io.github.kuggek.engine.GameEngine;
import io.github.kuggek.engine.ecs.GameScene;

public interface GameEngineManager {
    GameEngine initializeEngine();
    GameScene loadScene(String sceneName);
    boolean saveCurrentSceneAs(String sceneName);
    boolean saveCurrentScene();
    GameEngine openProject(String projectPath);
    boolean saveCurrentProject();
}
