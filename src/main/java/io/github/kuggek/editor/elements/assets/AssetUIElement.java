package io.github.kuggek.editor.elements.assets;

import javafx.scene.control.Button;

public class AssetUIElement extends Button {
    private static final String ASSET_STYLE = """
        -fx-padding: 15px 35px 15px 35px;
        -fx-border-insets: 15px 35px 15px 35px;
        -fx-background-insets: 15px 35px 15px 35px;

        -fx-background-color: rgba(175, 175, 175, 0.75);
        -fx-background-radius: 5px;

        -fx-border-color: black;
        -fx-border-width: 1px;
        -fx-border-radius: 5px;
    """;
    
    private Asset asset;

    public AssetUIElement(Asset asset) {
        super(asset.getID() + ": " + asset.getName());
        this.asset = asset;
        setStyle(ASSET_STYLE);
    }

    public Asset getAsset() {
        return asset;
    }
}
