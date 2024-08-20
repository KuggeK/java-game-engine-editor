package io.github.kuggek.editor.assets.managers;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.kuggek.editor.elements.assets.Asset;
import io.github.kuggek.engine.core.assets.SQLiteAssetManager;
import io.github.kuggek.engine.rendering.objects.Mesh;

public class MeshManager {

    private boolean isInitialized = false;
    private Map<Integer, Asset> assets;
    private SQLiteAssetManager manager;

    public MeshManager() {
        manager = SQLiteAssetManager.getInstance();
        assets = new HashMap<>();
    }

    public String getName() {
        return "Meshes";
    }

    public Set<Asset> getMeshAssets() {
        if (!isInitialized) {
            refreshMeshes();
            isInitialized = true;
        }
        return Set.copyOf(assets.values());
    }

    public Set<Asset> refreshMeshes() {
        assets.clear();
        try {
            ResultSet rs = manager.fetchAllMeshesRaw();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Asset asset = new Asset(id, name);
                assets.put(id, asset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Set.copyOf(assets.values());
    }

    public void deleteMesh(int id) {
        try {
            manager.deleteMesh(id);
            assets.remove(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateMesh(int meshID, Mesh updatedMesh) {
        try {
            manager.updateMesh(meshID, updatedMesh);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to update mesh: " + e.getMessage());
            return false;
        }
    }

    public boolean createMesh(Mesh mesh) {
        try {
            manager.saveMesh(mesh);
            assets.put(mesh.getID(), new Asset(mesh.getID(), mesh.getName()));
            return true;
        } catch (Exception e) {
            System.out.println("Failed to create mesh: " + e.getMessage());
            return false;
        }
    }

    public Mesh getMesh(int id) {
        try {
            return manager.fetchMesh(id);
        } catch (Exception e) {
            System.out.println("Failed to fetch mesh: " + e.getMessage());
            return null;
        }
    }

    public Mesh getMesh(Asset asset) {
        return getMesh(asset.getID());
    }
}
