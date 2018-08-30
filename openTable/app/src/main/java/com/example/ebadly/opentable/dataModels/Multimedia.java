package com.example.ebadly.opentable.dataModels;

public class Multimedia {
    private String type;
    private String src;
    private int width;
    private int height;

    public Multimedia(){}

    public Multimedia(String type, String src, int width, int height) {
        this.type = type;
        this.src = src;
        this.width = width;
        this.height = height;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getSrc() { return src; }

    public void setSrc(String src) { this.src = src; }

    public int getWidth() { return width; }

    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }

    public void setHeight(int height) { this.height = height; }
}
