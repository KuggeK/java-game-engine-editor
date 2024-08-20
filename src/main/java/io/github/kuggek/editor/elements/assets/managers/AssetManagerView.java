package io.github.kuggek.editor.elements.assets.managers;

import java.util.Set;

import io.github.kuggek.editor.elements.assets.Asset;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;

public interface AssetManagerView {
    String getName();
    Set<Asset> getAssets();
    Set<Asset> refreshAssets();
    Pane getSettingsView(Asset asset, EventHandler<ActionEvent> onClose);
    Pane getNewAssetView(EventHandler<ActionEvent> onClose);
}
