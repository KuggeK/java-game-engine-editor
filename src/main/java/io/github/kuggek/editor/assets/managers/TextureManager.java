package io.github.kuggek.editor.assets.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.kuggek.editor.elements.assets.Asset;
import io.github.kuggek.engine.core.assets.SQLiteAssetManager;

public class TextureManager {

    private SQLiteAssetManager manager;
    private Map<Integer, Asset> assets = new HashMap<>();
    private boolean isInitialized = false;

    public TextureManager() {
        manager = SQLiteAssetManager.getInstance();
        assets = new HashMap<>();
    }

    public String getName() {
        return "Textures";
    }

    public Set<Asset> getAssets() {
        if (!isInitialized) {
            refreshAssets();
            isInitialized = true;
        }

        return Set.copyOf(assets.values());
    }

    public Set<Asset> refreshAssets() {
        assets.clear();

        try {
            ResultSet rs = manager.fetchAllTexturesRaw();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                Asset asset = new Asset(id, "Texture " + id);
                assets.put(id, asset);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Set.copyOf(assets.values());
    }
}
