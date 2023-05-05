package com.wuav.client.gui.dto;

import java.io.File;

public class ImageDTO {
    private int id;
    private File file;

    private boolean isMain;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "id=" + id +
                ", file=" + file +
                ", isMain=" + isMain +
                '}';
    }
}
