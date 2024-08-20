package io.github.kuggek.editor.elements.gameobject.gamecomponent;

import io.github.kuggek.engine.ecs.GameObject;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GOTagsEditorField extends VBox {
    private GameObject gameObject;

    private Button addButton;

    private boolean updated = false;

    public GOTagsEditorField(GameObject gameObject) {
        this();
        setGO(gameObject);
    }

    public GOTagsEditorField() {
        addButton = new Button("Add Tag");
        addButton.setOnAction(e -> {
            gameObject.addTag("New Tag");
            setGO(gameObject);
        });
        getChildren().add(addButton);
    }

    public void setGO(GameObject go) {
        gameObject = go;
        getChildren().remove(1, getChildren().size());

        for (String s : gameObject.getTags()) {
            HBox field = new HBox();
            TextField textField = new TextField(s);
            Button removeButton = new Button("❌");

            Region filler = new Region();
            HBox.setHgrow(filler, Priority.ALWAYS);

            field.getChildren().addAll(textField, filler, removeButton);

            textField.setOnAction(e -> {
                updateTag(s, textField.getText());
            });

            textField.focusedProperty().addListener((_obs, _oldV, focused) -> {
                if (!focused) {
                    if (!updated) {
                        textField.setText(s); 
                    }
                    updated = false;
                }
            });

            removeButton.setOnAction(e -> {
                gameObject.removeTag(s);
                getChildren().remove(field);
            });

            getChildren().add(field);
        }
    }

    private void updateTag(String oldTag, String newTag) {
        gameObject.removeTag(oldTag);
        gameObject.addTag(newTag);
        updated = true;
    }

}
