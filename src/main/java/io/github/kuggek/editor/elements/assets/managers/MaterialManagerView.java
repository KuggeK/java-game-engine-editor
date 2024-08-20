package io.github.kuggek.editor.elements.assets.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.joml.Vector4f;

import io.github.kuggek.editor.elements.assets.Asset;
import io.github.kuggek.editor.elements.assets.settings.material.MaterialEditSettings;
import io.github.kuggek.engine.core.assets.SQLiteAssetManager;
import io.github.kuggek.engine.rendering.objects.Material;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;

public class MaterialManagerView implements AssetManagerView {

    private MaterialEditSettings settings;
    private boolean initialized = false;
    private Map<Integer, Asset> materialAssets;
    private SQLiteAssetManager assetManager;

    private final EventHandler<MaterialEditSettings.MaterialEvent> onUpdate = event -> {
        Material mat = event.getMaterial();
        try {
            assetManager.updateMaterial(mat.getID(), mat);
            materialAssets.get(mat.getID()).setName(mat.getName());
        } catch (Exception e) {
            System.out.println("Failed to update material with ID: " + mat.getID());
        }
        refreshAssets();
    };

    private final EventHandler<MaterialEditSettings.MaterialEvent> onSaveNewMaterial = event -> {
        Material mat = event.getMaterial();
        try {
            assetManager.saveMaterial(mat);
        } catch (Exception e) {
            System.out.println("Failed to save new material");
        }
        refreshAssets();
    };

    private final EventHandler<MaterialEditSettings.MaterialEvent> onDelete = event -> {
        Material mat = event.getMaterial();
        try {
            assetManager.deleteMaterial(mat.getID());
        } catch (Exception e) {
            System.out.println("Failed to delete material with ID: " + mat.getID());
        }
        refreshAssets();
    };

    public MaterialManagerView() {
        assetManager = SQLiteAssetManager.getInstance();
        materialAssets = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Materials";
    }

    @Override
    public Set<Asset> getAssets() {
        if (!initialized) {
            refreshAssets();
        }
        return Set.copyOf(materialAssets.values());
    }

    @Override
    public Set<Asset> refreshAssets() {
        materialAssets.clear();

        try (ResultSet rs = assetManager.fetchAllMaterialsRaw()) {
            while (rs.next()) {
                Asset matAsset = new Asset(rs.getInt("id"), rs.getString("name"));
                materialAssets.put(matAsset.getID(), matAsset);
            }
            initialized = true;
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return getAssets();
    }

    @Override
    public Pane getSettingsView(Asset asset, EventHandler<ActionEvent> onClose) {
        if (settings == null) {
            settings = new MaterialEditSettings();
            settings.setOnClose(e -> onClose.handle(new ActionEvent()));
        }

        settings.setOnSave(e -> {
            onUpdate.handle(e);
            onClose.handle(new ActionEvent());
        });
        settings.setOnDelete(e -> {
            onDelete.handle(e);
            onClose.handle(new ActionEvent());
        });
        
        try {
            Material mat = assetManager.fetchMaterial(asset.getID());
            settings.setMaterial(mat);
        } catch (Exception e) {
            System.out.println("Failed to fetch material with ID: " + asset.getID());
            return null;
        }

        return settings;
    }

    @Override
    public Pane getNewAssetView(EventHandler<ActionEvent> onClose) {
        if (settings == null) {
            settings = new MaterialEditSettings();
            settings.setOnClose(e -> onClose.handle(new ActionEvent()));
        }

        settings.setOnSave(e -> {
            onSaveNewMaterial.handle(e);
            onClose.handle(new ActionEvent());
        });
        settings.setOnDelete(null);

        settings.setMaterial(new Material(-1, "New Material", new Vector4f(), new Vector4f(), new Vector4f(), 0));

        return settings;
    }    
}
