package io.github.kuggek.editor.elements.assets;

public class Asset {
    private int ID;
    private String name;

    public Asset(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }
}
