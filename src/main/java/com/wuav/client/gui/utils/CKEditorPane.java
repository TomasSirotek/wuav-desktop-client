package com.wuav.client.gui.utils;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

public class CKEditorPane extends VBox {

    private WebView webView;
    private WebEngine webEngine;
    private StringProperty editorContent;

    public CKEditorPane() {
        webView = new WebView();
        webEngine = webView.getEngine();
        editorContent = new SimpleStringProperty();

        String ckEditorCDN = "https://cdn.ckeditor.com/4.16.1/standard/ckeditor.js";

        String html = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"utf-8\">" +
                "<script src=\"" + ckEditorCDN + "\"></script>" +
                "<style>" +
                "body { margin: 0; padding: 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<textarea name=\"editor1\" id=\"editor1\" rows=\"10\" cols=\"80\">" +
                "This is my sample text." +
                "</textarea>" +
                "<script>" +
                "CKEDITOR.replace( 'editor1', {" +
                "   toolbarGroups: [" +
                "       { name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] }," +
                "       { name: 'links' }," +
                "       { name: 'insert' }" +
                "   ]," +
                "   removeButtons: 'Underline,Strike,Subscript,Superscript,Anchor,Styles,Specialchar'," +
                "   format_tags: 'p;h1;h2;h3;pre'," +
                "   removeDialogTabs: 'image:advanced;link:advanced'," +
                "   filebrowserUploadUrl: '/uploader/upload.php'," +
                "   filebrowserBrowseUrl: '/browser/browse.php'," +
                "   resize_enabled: false," +
                "   height: '88px'" +
                "});" +
                "</script>" +
                "</body>" +
                "</html>";

        MFXButton imageBtn = new MFXButton("Add Image");
        imageBtn.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");

        imageBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                String imageUrl = selectedFile.toURI().toString();
                webEngine.executeScript("CKEDITOR.instances.editor1.insertHtml('<img src=\"" + imageUrl + "\" width=\"50\" height=\"50\" />');");
            }
        });

        webEngine.loadContent(html);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            editorContent.set((String) webEngine.executeScript("CKEDITOR.instances.editor1.getData();"));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        VBox vBox = new VBox();
        VBox.setMargin(imageBtn, new Insets(10, 0, 0, 0));
        imageBtn.setPrefWidth(385);
        imageBtn.setMinWidth(385);
        vBox.setPadding(new Insets(10, 0, 0, 0)); // top-right-bottom-left
        vBox.getChildren().addAll(webView, imageBtn);

        getChildren().add(vBox);
    }

    public StringProperty editorContentProperty() {
        return editorContent;
    }

    public void setContent(String content) {
        String escapedContent = content.replace("'", "\\'");
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    webEngine.executeScript("CKEDITOR.instances.editor1.setData('" + escapedContent + "');");
                    webEngine.getLoadWorker().stateProperty().removeListener(this);  // remove listener to avoid multiple calls
                }
            }
        });
    }

}