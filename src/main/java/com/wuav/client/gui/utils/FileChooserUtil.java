package com.wuav.client.gui.utils;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class FileChooserUtil {

    private FileChooser fileChooser;
    private String title;
    private String extensions;
    private String initialFileName;

    public FileChooserUtil(String title, String extensions, String initialFileName) {
        this.title = title;
        this.extensions = extensions;
        this.initialFileName = initialFileName;
        this.fileChooser = new FileChooser();

        setupFileChooser();
    }

    private void setupFileChooser() {
        fileChooser.setTitle(title);
        fileChooser.setInitialFileName(initialFileName);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extensions, extensions);
        fileChooser.getExtensionFilters().add(extFilter);
    }

    public File showDialog(Window window){
        return fileChooser.showSaveDialog(window);
    }
}
