package io.github.kuggek.editor.elements.assets.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.kuggek.editor.elements.assets.Asset;
import io.github.kuggek.editor.elements.assets.BasicPopup;
import io.github.kuggek.editor.elements.assets.settings.mesh.MeshEditSettings;
import io.github.kuggek.editor.elements.assets.settings.mesh.MeshEditSettings.MeshEvent;
import io.github.kuggek.engine.core.assets.SQLiteAssetManager;
import io.github.kuggek.engine.rendering.objects.Mesh;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;

public class MeshManagerView implements AssetManagerView {
    
    private SQLiteAssetManager manager;
    private MeshEditSettings settings;
    private final BasicPopup popup = new BasicPopup();

    private Map<Integer, Asset> meshAssets;

    private boolean initialized = false;

    private final EventHandler<MeshEvent> onNewSave = e -> {
        Mesh mesh = e.getMesh();
        saveMesh(mesh);
    };

    private final EventHandler<MeshEvent> onSave = e -> {
        Mesh mesh = e.getMesh();
        updateMesh(mesh);
    };

    private final EventHandler<MeshEvent> onDelete = e -> {
        deleteMesh(e.getMesh());
        popup.hide();
    };

    public MeshManagerView() {
        manager = SQLiteAssetManager.getInstance();
        meshAssets = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Meshes";
    }

    @Override
    public Set<Asset> getAssets() {
        if (!initialized) {
            refreshAssets();
            initialized = true;
        }
        return Set.copyOf(meshAssets.values());
    }

    @Override
    public Set<Asset> refreshAssets() {
        meshAssets.clear();
        ResultSet rs;
        try {
            rs = manager.fetchAllMeshesRaw();
        } catch (SQLException e) {
            System.out.println("Failed to fetch meshes: " + e.getMessage());
            return Set.copyOf(meshAssets.values());
        }
        try {
            while (rs.next()) {
                try {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    meshAssets.put(id, new Asset(id, name));
                } catch (SQLException e) {
                    System.out.println("Failed to fetch a mesh: " + e.getMessage());
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong while iterating over the result set: " + e.getMessage());
        }
        return Set.copyOf(meshAssets.values());
    }

    @Override
    public Pane getSettingsView(Asset asset, EventHandler<ActionEvent> onClose) {
        Mesh mesh = getMesh(asset);
        if (mesh == null) {
            return null;
        }
        settings = new MeshEditSettings(mesh);
        settings.setOnSave(e -> {
            onSave.handle(e);
            onClose.handle(new ActionEvent());
        });
        settings.setOnDelete(e -> {
            onDelete.handle(e);
            onClose.handle(new ActionEvent());
        });
        settings.setOnClose(e -> onClose.handle(new ActionEvent()));
        return settings;
    }

    @Override
    public Pane getNewAssetView(EventHandler<ActionEvent> onClose) {
        settings = new MeshEditSettings();
        settings.setOnSave(e -> {
            onNewSave.handle(e);
            onClose.handle(new ActionEvent());
        });
        settings.setOnClose(e -> onClose.handle(new ActionEvent()));
        return settings;
    }

    private Mesh getMesh(int ID) {
        try {
            return manager.fetchMesh(ID);
        } catch (Exception e) {
            System.out.println("Failed to get mesh: " + e.getMessage());
            return null;
        }
    }

    private Mesh getMesh(Asset asset) {
        return getMesh(asset.getID());
    }

    private void saveMesh(Mesh mesh) {
        try {
            manager.saveMesh(mesh);
            meshAssets.put(mesh.getID(), new Asset(mesh.getID(), mesh.getName()));
        } catch (Exception e) {
            System.out.println("Failed to save mesh: " + e.getMessage());
        }
    }

    private void updateMesh(Mesh mesh) {
        try {
            manager.updateMesh(mesh.getID(), mesh);
            meshAssets.get(mesh.getID()).setName(mesh.getName());
        } catch (Exception e) {
            System.out.println("Failed to update mesh: " + e.getMessage());
        }
    }

    private void deleteMesh(Mesh mesh) {
        try {
            manager.deleteMesh(mesh.getID());
            meshAssets.remove(mesh.getID());
        } catch (Exception e) {
            System.out.println("Failed to delete mesh: " + e.getMessage());
        }
    }
}
