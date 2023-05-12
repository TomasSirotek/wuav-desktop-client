package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.utilities.AlertHelper;
import com.wuav.client.bll.utilities.pdf.DefaultPdfGenerator;
import com.wuav.client.bll.utilities.pdf.IPdfGenerator;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.models.user.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PDFBuilderController extends RootController implements Initializable {
    @FXML
    private MFXCheckbox descriptionCheck;
    @FXML
    private MFXCheckbox technicianCheck;
    @FXML
    private MFXCheckbox deviceCheck;
    @FXML
    private MFXCheckbox photosCheck;
    @FXML
    private MFXButton preview;
    @FXML
    private MFXTextField fileName;
    @FXML
    private MFXButton export;

    @FXML
    private HBox loadingBox;
    @FXML
    private Pagination pagination;

    @FXML
    private Label projectName;

    @FXML
    private AnchorPane builderAnchorPane;

    private Project project;
    private final EventBus eventBus;
    private Image defaultImage = new Image("/pdf.png");

    private final IPdfGenerator pdfGenerator;

    private ObjectProperty<ByteArrayOutputStream> finalPDFBytesProperty = new SimpleObjectProperty<>();

    private BooleanProperty isExport = new SimpleBooleanProperty(true); // You can set this to true or false as needed

    @Inject
    public PDFBuilderController(EventBus eventBus, IPdfGenerator pdfGenerator) {
        this.eventBus = eventBus;
        this.pdfGenerator = pdfGenerator;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventBus.register(this);
        // Bind the text property of the actionButton to the isExport property
        export.textProperty().bind(Bindings.when(isExport)
                .then("Export")
                .otherwise("Send Email"));

        // Disable the actionButton if we're in export mode and either the final PDF bytes are null or the filename is empty
        export.disableProperty().bind(
                isExport.and(
                        finalPDFBytesProperty.isNull()
                                .or(fileName.textProperty().isEmpty())
                )
        );

        // Set the onAction of the actionButton to call either the export or email method, depending on the isExport property
        export.setOnAction(event -> {
            if (isExport.get()) {
                exportPDF();
            } else {
                sendEmail();
            }
        });

        Platform.runLater(() -> {
            Stage stage = (Stage) builderAnchorPane.getScene().getWindow();
            var project = (Project) stage.getProperties().get("projectToExport");
            if (project != null) {
                projectName.setText(project.getName());
                this.project = project;

                // Generate the default PDF in a background task
                Task<Image> generateDefaultPdfTask = new Task<>() {
                    @Override
                    protected Image call() throws Exception {
                        loadingBox.setVisible(true);
                        AppUser appUser = CurrentUser.getInstance().getLoggedUser();

                        DefaultPdfGenerator pdfGenerator = new DefaultPdfGenerator.Builder(appUser, project, "default")
                                .includeDescription(false)
                                .includeTechnicians(false)
                                .includeDevices(false)
                                .includeImages(false)
                                .build();

                        ByteArrayOutputStream defaultPdf = pdfGenerator.generatePdf();
                        return convertPdfToImage(defaultPdf);
                    }
                };

                // When the task is done, update the ImageView with the default PDF
                generateDefaultPdfTask.setOnSucceeded(event -> {
                    loadingBox.setVisible(false);
                    System.out.println("Task succeeded");
                    Image defaultPdfImage = generateDefaultPdfTask.getValue();
                    pagination.setPageFactory((pageIndex) -> {
                        ImageView imageView = new ImageView(defaultPdfImage);
                        imageView.setFitWidth(300);  // Set the desired width
                        imageView.setFitHeight(300); // Set the desired height
                        imageView.setPreserveRatio(true); // This will maintain the image's aspect ratio
                        return imageView;
                    });
                });

                // Start the task
                new Thread(generateDefaultPdfTask).start();
            }

            // Setup the preview button
            preview.setOnAction(event -> {
                boolean includeDescription = descriptionCheck.isSelected();
                boolean includeTechnicians = technicianCheck.isSelected();
                boolean includeDevices = deviceCheck.isSelected();
                boolean includeImages = photosCheck.isSelected();
                AppUser appUser = CurrentUser.getInstance().getLoggedUser();
                loadingBox.setVisible(true);

                DefaultPdfGenerator pdfGenerator = new DefaultPdfGenerator.Builder(appUser, project, "preview")
                        .includeDescription(includeDescription)
                        .includeTechnicians(includeTechnicians)
                        .includeDevices(includeDevices)
                        .includeImages(includeImages)
                        .build();

                Task<ByteArrayOutputStream> generatePdfTask = new Task<>() {
                    @Override
                    protected ByteArrayOutputStream call() throws Exception {
                        return pdfGenerator.generatePdf();
                    }
                };

                generatePdfTask.setOnSucceeded(pdfEvent -> {
                    loadingBox.setVisible(false);
                    ByteArrayOutputStream pdfStream = generatePdfTask.getValue();
                    // set it to the instance variable
                    finalPDFBytesProperty.set(pdfStream);
                    List<Image> pdfImages = null;
                    try {
                        pdfImages = convertPdfToImages(pdfStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    List<Image> finalPdfImages = pdfImages;
                    pagination.setPageFactory((pageIndex) -> {
                        ImageView imageView = new ImageView(finalPdfImages.get(pageIndex));
                        imageView.setFitWidth(300);  // Set the desired width
                        imageView.setFitHeight(300); // Set the desired height
                        imageView.setPreserveRatio(true); // This will maintain the image's aspect ratio
                        return imageView;
                    });
                    pagination.setPageCount(pdfImages.size());
                });

                new Thread(generatePdfTask).start();
            });
        });
    }



    private Image convertPdfToImage(ByteArrayOutputStream pdfStream) {
        try (InputStream in = new ByteArrayInputStream(pdfStream.toByteArray())) {
            PDDocument document = PDDocument.load(in);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bImage = pdfRenderer.renderImageWithDPI(0, 300); // 0 = first page
            document.close();
            return SwingFXUtils.toFXImage(bImage, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Image> convertPdfToImages(ByteArrayOutputStream pdfStream) throws IOException {
        List<Image> images = new ArrayList<>();
        PDDocument document = PDDocument.load(pdfStream.toByteArray());
        PDFRenderer renderer = new PDFRenderer(document);

        for (int i = 0; i < document.getNumberOfPages(); ++i) {
            BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 300);
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            images.add(fxImage);
        }

        document.close();
        return images;
    }

// handle is email
    @Subscribe
    public void handleIsEmail(RefreshEvent event) {
        if (event.eventType() == EventType.EXPORT_EMAIL) {
            isExport.set(false);
            System.out.println(isExport.get());
        }
    }

  // ACTIONS

    private void exportPDF() {
        FileChooser fileChooser = new FileChooser();

        // Set the title for the save dialog
        fileChooser.setTitle("Save PDF");

        // Set the initial file name
        fileChooser.setInitialFileName(fileName.getText() + ".pdf");

        // Set the file type filter to show only PDF files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the save dialog and get the selected file
        File file = fileChooser.showSaveDialog(builderAnchorPane.getScene().getWindow());

        // If the user has selected a file
        if (file != null) {
            try {
                // Write the ByteArrayOutputStream to the selected file
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    finalPDFBytesProperty.getValue().writeTo(fileOutputStream);
                    AlertHelper.showDefaultAlert("Success !" , Alert.AlertType.CONFIRMATION);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendEmail() {
        System.out.println("Sending email");
    }


}
