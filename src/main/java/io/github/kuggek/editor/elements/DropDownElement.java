package io.github.kuggek.editor.elements;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class DropDownElement<T extends Node> extends VBox {
    
    // The header of the dropdown
    protected HBox header;
    protected Label titleLabel;
    private Region headerFiller;
    protected ToggleButton ddButton;

    private int ddMargin;

    private String headerStyle = """
        -fx-background-color: rgb(50, 50, 50);
        -fx-border-color: rgb(25, 25, 25);
        -fx-border-width: 1px;
        -fx-border-radius: 10px;
        -fx-padding: 5px;
        -fx-text-fill: rgb(200, 200, 200);
    """;

    private String ddButtonStyle = """
        -fx-background-color: rgba(100, 100, 100, 0.0);
        -fx-border-color: rgb(200, 200, 200);
        -fx-border-width: 1px;
        -fx-border-radius: 100000px;
        -fx-text-fill: rgb(200, 200, 200);
    """;

    private String containerStyle = """
        -fx-border-color: rgb(50, 50, 50);
        -fx-border-width: 1px;
        -fx-border-radius: 10px;
        -fx-padding: 10px;
    """;

    // The element that will be shown when the dropdown is expanded
    private T ddElement;

    private ContextMenu contextMenu;

    public DropDownElement(String title, T ddElement, int ddMargin) {
        titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: rgb(200, 200, 200);");
        
        this.ddMargin = ddMargin;

        header = new HBox();
        header.setStyle(headerStyle);

        headerFiller = new Region();
        HBox.setHgrow(headerFiller, Priority.ALWAYS);

        ddButton = new ToggleButton(">");
        ddButton.setMinSize(20, 20);
        ddButton.setMaxSize(20, 20);
        ddButton.setStyle(ddButtonStyle);
        ddButton.setSelected(false);
        ddButton.setAlignment(Pos.CENTER);
        ddButton.setFont(Font.font(8.5));
        ddButton.setOnAction(e -> {
            updateDropDown();
        });
        updateDropDown();

        // Set element attributes
        setMinSize(100, 20);
        setStyle(containerStyle);

        header.getChildren().addAll(this.titleLabel, headerFiller, ddButton);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        getChildren().addAll(header);

        setDdElement(ddElement);
    }

    public DropDownElement(String title, T ddElement) {
        this(title, ddElement, 10);
    }

    public DropDownElement(String title) {
        this(title, null);
    }

    public void setDdElement(T ddElement) {
        if (this.ddElement != null) {
            getChildren().remove(this.ddElement);
        }

        this.ddElement = ddElement;

        if (ddElement != null) {
            setMargin(ddElement, new Insets(0, 0, 0, ddMargin));
        }

        updateDropDown();
    }

    private void updateDropDown() {
        if (ddElement == null) {
            ddButton.setVisible(false);
            return;
        } 
        
        ddButton.setVisible(true);
        boolean contains = getChildren().contains(ddElement);
        if (ddButton.isSelected()) {
            if (!contains) {
                getChildren().add(ddElement);
            }
            ddButton.setText("v");
        } else {
            if (contains) {
                getChildren().remove(ddElement);
            }
            ddButton.setText(">");
        }
    }

    public void open() {
        ddButton.setSelected(true);
        updateDropDown();
    }

    public void close() {
        ddButton.setSelected(false);
        updateDropDown();
    }

    public void toggleDropDown() {
        ddButton.setSelected(!ddButton.isSelected());
        updateDropDown();
    }

    public void setDropDownMargin(int margin) {
        if (ddElement != null) {
            setMargin(ddElement, new Insets(0, 0, 0, margin));
        }
    }
    
    public HBox getHeader() {
        return header;
    }

    public void setContextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
        header.setOnContextMenuRequested(e -> {
            if (contextMenu == null) {
                return;
            }
            contextMenu.show(header, e.getScreenX(), e.getScreenY() + header.getHeight());
        });
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
