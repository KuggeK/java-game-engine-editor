package io.github.kuggek.editor.elements;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.util.EventListener;

import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;

import io.github.kuggek.engine.rendering.Window;
import io.github.kuggek.engine.rendering.WindowSettings;
import io.github.kuggek.engine.scripting.KeyInput;

public class GLPanel extends GLJPanel implements Window {

    private KeyInput keyInput;

    public GLPanel() {
        super();
        keyInput = new KeyInput();
        setDefaultCloseOperation(WindowClosingMode.DISPOSE_ON_CLOSE);
        addKeyListener(keyInput.getKeyRegisterer());
        setSkipGLOrientationVerticalFlip(!isGLOriented());

        setMinimumSize(new Dimension(360, 240));
    }

    @Override
    public void setWindowSettings(WindowSettings settings) {
        setSize(settings.getWidth(), settings.getHeight());
    }

    @Override
    public void registerEventListener(EventListener listener) {
        if (listener instanceof KeyListener) {
            addKeyListener((KeyListener)listener);
        } 
        else if (listener instanceof GLEventListener) {
            addGLEventListener((GLEventListener) listener);
        } 
        else {
            throw new IllegalArgumentException("Unsupported EventListener type");
        }
    }

    @Override
    public void removeEventListener(EventListener listener) {
        if (listener instanceof KeyListener) {
            removeKeyListener((KeyListener)listener);
        }
        else if (listener instanceof GLEventListener) {
            removeGLEventListener((GLEventListener) listener);
        }
        else {
            throw new IllegalArgumentException("Unsupported EventListener type");
        }
    }

    @Override
    public void toggleFullscreen() {
        throw new UnsupportedOperationException("Cannot toggle fullscreen on this component");
    }

    @Override
    public KeyInput getKeyInput() {
        return keyInput;
    }
}
