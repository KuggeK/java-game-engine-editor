package io.github.kuggek.editor.elements.assets.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.kuggek.editor.elements.assets.Asset;
import io.github.kuggek.editor.elements.assets.settings.texture.TextureEditSettings;
import io.github.kuggek.engine.core.assets.SQLiteAssetManager;
import io.github.kuggek.engine.rendering.objects.Texture;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;

public class TextureManagerView implements AssetManagerView {

    private Map<Integer, Asset> textureAssets;
    private boolean initialized = false;
    private SQLiteAssetManager assetManager;
    private TextureEditSettings settings;

    private EventHandler<TextureEditSettings.TextureEvent> newOnSave = e -> {
        Texture texture = e.getTexture();
        saveTexture(texture);
    };

    private EventHandler<TextureEditSettings.TextureEvent> onSave = e -> {
        Texture texture = e.getTexture();
        updateTexture(texture);
    };

    private EventHandler<TextureEditSettings.TextureEvent> onDelete = e -> {
        deleteTexture(e.getTexture());
    };

    public TextureManagerView() {
        textureAssets = new HashMap<>();
        assetManager = SQLiteAssetManager.getInstance();
    }

    @Override
    public String getName() {
        return "Textures";
    }

    @Override
    public Set<Asset> getAssets() {
        if (!initialized) {
            refreshAssets();
        }
        return Set.copyOf(textureAssets.values());
    }

    @Override
    public Set<Asset> refreshAssets() {
        textureAssets.clear();
        try (ResultSet rs = assetManager.fetchAllTexturesRaw()) {
            try {
                while (rs.next()) {
                    Asset asset = new Asset(rs.getInt("id"), rs.getString("name"));
                    textureAssets.put(asset.getID(), asset);
                }
                initialized = true;
                rs.close();

            } catch (Exception e) {
                System.out.println("Error fetching textures: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error fetching textures: " + e.getMessage());
        }

        return Set.copyOf(textureAssets.values());
    }

    @Override
    public Pane getSettingsView(Asset asset, EventHandler<ActionEvent> onCloseParent) {
        Texture texture = null;
        try {
            texture = assetManager.fetchTexture(asset.getID());

        } catch (Exception e) {
            System.out.println("Error fetching texture: " + e.getMessage());
            return null;
        }

        settings = new TextureEditSettings();
        settings.setTexture(texture);
        settings.setVisible(true);
        settings.setOnSave(e -> {
            onSave.handle(e);
            onCloseParent.handle(new ActionEvent());
        });
        settings.setOnDelete(e -> {
            onDelete.handle(e);
            onCloseParent.handle(new ActionEvent());
        });
        settings.setOnClose(e -> onCloseParent.handle(new ActionEvent()));
        return settings;
    }

    @Override
    public Pane getNewAssetView(EventHandler<ActionEvent> onClose) {
        settings = new TextureEditSettings();
        settings.setOnSave(e -> {
            newOnSave.handle(e);
            onClose.handle(new ActionEvent());
        });
        settings.setOnClose(e -> onClose.handle(new ActionEvent()));
        return settings;
    }

    private void saveTexture(Texture texture) {
        try {
            assetManager.saveTexture(texture);
            refreshAssets();
        } catch (Exception e) {
            System.out.println("Error saving texture: " + e.getMessage());
        }
    }

    private void updateTexture(Texture texture) {
        try {
            assetManager.updateTexture(texture.getID(), texture);
            refreshAssets();
        } catch (Exception e) {
            System.out.println("Error updating texture: " + e.getMessage());
        }
    }

    private void deleteTexture(Texture texture) {
        try {
            assetManager.deleteTexture(texture.getID());
            refreshAssets();
        } catch (Exception e) {
            System.out.println("Error deleting texture: " + e.getMessage());
        }
    }
}
