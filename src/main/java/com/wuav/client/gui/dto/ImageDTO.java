package com.wuav.client.gui.dto;

import java.io.File;

/**
 * The record ImageDTO.
 */
public class ImageDTO {
    private int id;
    private File file;

    private boolean isMain;


    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets file.
     *
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets file.
     *
     * @param file the file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Is main boolean.
     *
     * @return the boolean
     */
    public boolean isMain() {
        return isMain;
    }

    /**
     * Sets main.
     *
     * @param main the main
     */
    public void setMain(boolean main) {
        isMain = main;
    }

    /**
     * To string
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ImageDTO{" +
                "id=" + id +
                ", file=" + file +
                ", isMain=" + isMain +
                '}';
    }
}
