package com.wuav.client.gui.utils;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

/**
 * The type File chooser util.
 */
public class FileChooserUtil {

    private FileChooser fileChooser;
    private String title;
    private String extensions;
    private String initialFileName;

    /**
     * Instantiates a new File chooser util.
     *
     * @param title           the title
     * @param extensions      the extensions
     * @param initialFileName the initial file name
     */
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

    /**
     * Show dialog file.
     *
     * @param window the window
     * @return the file
     */
    public File showDialog(Window window){
        return fileChooser.showSaveDialog(window);
    }
}
