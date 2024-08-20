package io.github.kuggek.editor.elements.assets;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.github.kuggek.editor.elements.assets.managers.AssetManagerView;
import io.github.kuggek.editor.elements.assets.managers.MaterialManagerView;
import io.github.kuggek.editor.elements.assets.managers.MeshManagerView;
import io.github.kuggek.editor.elements.assets.managers.TextureManagerView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AssetInspector extends HBox {

    private final GridPane assetList;
    private final VBox assetManagerList;
    private final Button refreshButton;
    private final Button newAssetButton;
    
    private final Region filler;

    private Button currentManagerButton;

    private BasicPopup settingsPopup;

    private int columnCount = 3;

    private List<AssetManagerView> managers = List.of(
        new MeshManagerView(),
        new TextureManagerView(),
        new MaterialManagerView()
    );

    private AssetManagerView activeManager;

    private EventHandler<ActionEvent> onListtingsClose = e -> {
        settingsPopup.hide();
        refreshCurrentManager();
    };
    
    public AssetInspector() {
        assetList = new GridPane();
        
        assetManagerList = new VBox();
        for (AssetManagerView manager : managers) {
            Button mgrButton = new Button(manager.getName());
            mgrButton.setOnAction(e -> setCurrentManager(manager, mgrButton));
            assetManagerList.getChildren().add(mgrButton);
        }

        filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        setMinSize(100, 100);
        setMaxSize(900, 400);

        refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshCurrentManager());

        newAssetButton = new Button("New");
        newAssetButton.setOnAction(e -> openNewAssetDialog());

        getChildren().addAll(assetManagerList, assetList, filler, refreshButton, newAssetButton);

        settingsPopup = new BasicPopup();

        setCurrentManager(null);
    }

    private void setCurrentManager(AssetManagerView manager) {
        if (manager == null) {
            refreshButton.setDisable(true);
            newAssetButton.setDisable(true);
            assetList.getChildren().clear();
            return;
        }

        if (activeManager == manager) {
            return;
        }

        refreshButton.setDisable(false);
        newAssetButton.setDisable(false);

        activeManager = manager;

        List<AssetUIElement> assets = assetsToUIElements(activeManager.getAssets());
        populate(assets);
    }

    private void setCurrentManager(AssetManagerView manager, Button managerButton) {
        if (currentManagerButton != null) {
            currentManagerButton.setDisable(false);
        }
        currentManagerButton = managerButton;
        currentManagerButton.setDisable(true);

        // Request focus because after disabling the button, the focus is set to the next button 
        // and it looks ugly
        requestFocus();
        setCurrentManager(manager);
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public void refreshCurrentManager() {
        if (activeManager != null) {
            List<AssetUIElement> assets = assetsToUIElements(activeManager.refreshAssets());
            populate(assets);
        }
    }

    private List<AssetUIElement> assetsToUIElements(Collection<Asset> assets) {
        return assets.stream().sorted(Comparator.comparing(Asset::getID)).map(AssetUIElement::new).collect(Collectors.toList());
    }

    private void populate(List<AssetUIElement> assets) {
        assetList.getChildren().clear();
        int row = 0;
        int col = 0;
        for (AssetUIElement asset : assets) {
            asset.setOnAction(e -> openListtings(asset.getAsset()));
            assetList.add(asset, col, row);
            col++;
            if (col >= columnCount) {
                col = 0;
                row++;
            }
        }
    }

    private void openListtings(Asset asset) {
        Pane settings = activeManager.getSettingsView(asset, onListtingsClose);
        if (settings == null) {
            return;
        }

        settingsPopup.setContent(settings);
        settingsPopup.show();
    }

    private void openNewAssetDialog() {
        Pane newAssetView = activeManager.getNewAssetView(onListtingsClose);
        if (newAssetView == null) {
            return;
        }

        settingsPopup.setContent(newAssetView);
        settingsPopup.show();
    }
}
