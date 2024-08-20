package io.github.kuggek.editor.elements.gameobject;

import java.util.HashMap;
import java.util.Map;

import io.github.kuggek.engine.ecs.GameComponent;
import io.github.kuggek.engine.ecs.components.physics.PhysicsBodyComponent;
import io.github.kuggek.engine.ecs.components.physics.colliders.SphereColliderComponent;
import io.github.kuggek.engine.ecs.components.physics.colliders.BoxColliderComponent;
import io.github.kuggek.engine.ecs.components.rendering.CameraComponent;
import io.github.kuggek.engine.ecs.components.rendering.RenderInstanceComponent;
import io.github.kuggek.engine.ecs.components.rendering.lights.DirectionalLightComponent;
import io.github.kuggek.engine.ecs.components.rendering.lights.PositionalLightComponent;
import io.github.kuggek.engine.scripting.GenericControllerScript;
import io.github.kuggek.engine.scripting.JarClassLoader;
import io.github.kuggek.engine.scripting.ScriptLoader;

public class GameComponentTypes {
    

    public static final Map<String, Class<? extends GameComponent>> NATIVE_COMPONENTS = new HashMap<>();
    public static final Map<String, Class<? extends GameComponent>> SCRIPT_COMPONENTS = new HashMap<>();

    static {
        addComponentType(PhysicsBodyComponent.class);
        addComponentType(SphereColliderComponent.class);
        addComponentType(BoxColliderComponent.class);
        addComponentType(RenderInstanceComponent.class);
        addComponentType(GenericControllerScript.class);
        addComponentType(DirectionalLightComponent.class);
        addComponentType(PositionalLightComponent.class);
        addComponentType(CameraComponent.class);
    }

    public static void addComponentType(Class<? extends GameComponent> type) {
        NATIVE_COMPONENTS.put(type.getName(), type);
    }

    public static Class<? extends GameComponent> getComponentType(String name) {
        return NATIVE_COMPONENTS.get(name);
    }

    public static Map<String, Class<? extends GameComponent>> getComponentTypes() {
        return NATIVE_COMPONENTS;
    }

    /**
     * Adds all compiled scripts to the component types map. This should be called after compiling
     * all scripts. See {@link ScriptLoader#compileAllScripts()}.
     */
    @SuppressWarnings("unchecked")
    public static void addScripts() {
        JarClassLoader loader = ScriptLoader.getJarClassLoader();
        for (String scriptName : ScriptLoader.getLoadedScriptsNames()) {
            System.out.println("scriptName variable is: " + scriptName);
            try {
                Class<?> script = loader.loadClass(scriptName);
                if (GameComponent.class.isAssignableFrom(script)) {
                    SCRIPT_COMPONENTS.put(scriptName, (Class<? extends GameComponent>) script);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static Map<String, Class<? extends GameComponent>> getScriptComponents() {
        return SCRIPT_COMPONENTS;
    }

    public static Class<? extends GameComponent> getScriptComponent(String name) {
        return SCRIPT_COMPONENTS.get(name);
    }
}
