package org.imgedit.common;


public class ResizeImageInfo {
    private final int width;
    private final int height;
    private final String name;


    public ResizeImageInfo(int width, int height) {
        this(width, height, null);
    }

    public ResizeImageInfo(int width, int height, String name) {
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }
}
