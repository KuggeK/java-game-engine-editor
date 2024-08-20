package io.github.kuggek.editor.elements.assets.settings;

import io.github.kuggek.editor.elements.assets.Asset;
import javafx.scene.Node;

public interface AssetSettings {
    /**
     * Gets the view of the asset settings.
     * @return The view of the asset settings.
     */
     Node getView();

    /**
     * Opens a dialog to edit the asset.
     */
     void openEditDialog();

    /**
     * Deletes the asset.
     */
     void deleteAsset();

    /**
     * Sets the asset to edit.
     * @param asset The asset to edit.
     */
     void setAsset(Asset asset);

    /**
     * Gets the asset being edited.
     * @return The asset being edited.
     */
     Asset getAsset();
}
