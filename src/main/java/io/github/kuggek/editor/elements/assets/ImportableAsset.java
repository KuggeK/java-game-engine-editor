package io.github.kuggek.editor.elements.assets;

import javafx.stage.FileChooser;

public abstract class ImportableAsset extends Asset {

    public ImportableAsset(int ID, String name) {
        super(ID, name);
    }

    /**
     * Imports an asset from a file. Sets the file chooser to the correct settings for the asset type.
     * @param fileChooser the file chooser to set the settings for.
     */
    public abstract void importAsset(FileChooser fileChooser);

    /**
     * Updates the asset from a file. Sets the file chooser to the correct settings for the asset type.
     * @param fileChooser the file chooser to set the settings for.
     */
    public abstract void updateAsset(FileChooser fileChooser);
}
