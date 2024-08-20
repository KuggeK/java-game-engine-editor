package io.github.kuggek.editor.elements.assets.settings.mesh;

import java.io.File;

import javafx.stage.FileChooser;

public class MeshFileChooser {
    
    public static FileChooser get() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Mesh");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Wavefront OBJ", "*.obj")
        );
        fileChooser.setInitialDirectory(new File("./"));
        return fileChooser;
    }

}
