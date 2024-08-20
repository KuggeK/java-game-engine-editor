package io.github.kuggek.editor.elements.assets;

import javafx.event.Event;
import javafx.event.EventType;

public class AssetEvent extends Event {

    private static final EventType<AssetEvent> ASSET_EVENT = new EventType<>(Event.ANY, "ASSET_EVENT");

    private Asset asset;

    public AssetEvent(Asset asset) {
        super(ASSET_EVENT);
    }

    public Asset getAsset() {
        return asset;
    }
}
