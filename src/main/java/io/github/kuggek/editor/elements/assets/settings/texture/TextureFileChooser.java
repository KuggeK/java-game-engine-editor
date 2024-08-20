package io.github.kuggek.editor.elements.assets.settings.texture;

import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import javafx.stage.FileChooser;

public class TextureFileChooser {
    
    public static FileChooser get() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Texture");
        fileChooser.getExtensionFilters().addAll(
            Arrays.stream(ImageIO.getReaderFileSuffixes())
                .map(ext -> new FileChooser.ExtensionFilter(ext.toUpperCase() + " Image", "*." + ext))
                .toArray(FileChooser.ExtensionFilter[]::new)
        );
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setInitialDirectory(new File("./"));
        return fileChooser;
    }
}
