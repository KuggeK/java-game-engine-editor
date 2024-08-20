package io.github.kuggek.editor.elements.assets;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class BasicPopup {
    
    private Pane settingsContent;
    private Stage stage;
    private Scene scene;

    private final String POPUP_STYLE = """
        -fx-background-color: rgb(240, 240, 240);
        -fx-background-radius: 5px;

        -fx-padding: 10px;
    """;

    public BasicPopup() {
        settingsContent = new Pane();

        settingsContent.setPrefWidth(300);
        settingsContent.setPrefHeight(300);
        settingsContent.setMinHeight(300);
        settingsContent.setMinWidth(300);

        settingsContent.setStyle(POPUP_STYLE);

        scene = new Scene(settingsContent);

        stage = new Stage();
        stage.setScene(scene);
    }

    public void setContent(Node content) {
        settingsContent.getChildren().clear();
        settingsContent.getChildren().add(content);
    }

    public void addContent(Node content) {
        settingsContent.getChildren().add(content);
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }
}
