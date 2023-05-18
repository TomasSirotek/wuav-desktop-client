package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.be.device.Device;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.cache.ImageCache;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.dto.PutAddressDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.utils.AnimationUtil;
import com.wuav.client.gui.utils.CKEditorPane;
import com.wuav.client.gui.utils.enums.CustomColor;
import com.wuav.client.gui.utils.enums.IConType;
import com.wuav.client.gui.utils.event.CustomEvent;
import com.wuav.client.gui.utils.validations.FormField;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;


public class ProjectActionController  extends RootController implements Initializable {

    @FXML
    private Pane notificationPane;

    @FXML
    private ImageView notificationImage;
    @FXML
    private Label errorLabel,fileName,projectNameField;
    @FXML
    private MFXButton uploadBtn,cancelBtn,expand1,expand2,expandBtn,updateClient,updateBtnNotes,selectFile;
    @FXML
    private HBox newFileUploadBox;
    @FXML
    private VBox editorBox;
    @FXML
    private ImageView firstUploadedImage,secondUploadedImage,selectedImage;
    @FXML
    private MFXTextField clientNameField,clientEmailField,clientPhoneField,clientCityField;
    @FXML
    private MFXScrollPane deviceForProjectList;
    @FXML
    private ChoiceBox clientTypeChooseField;
    @FXML
    private TextField clientAddress;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab clientTab;

    private final IControllerFactory controllerFactory;

    private final IProjectModel projectModel;

    private Project currentProject;

    private Image mainImage;
    private final EventBus eventBus;

    private File selectedImageFile;

    private StringProperty editorContent = new SimpleStringProperty();
    private List<HBox> deviceDetailsList = new ArrayList<>();


    @Inject
    public ProjectActionController(IControllerFactory controllerFactory, IProjectModel projectModel, EventBus eventBus) {
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;
        this.eventBus = eventBus;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventBus.register(this);
        selectFile.setOnAction(e -> selectFile());
        expandBtn.setOnAction(e -> previewImage(selectedImage.getImage()));
        updateBtnNotes.setOnAction(e -> updateNotes());
        newFileUploadBox.setVisible(false);
        selectedImageFile = null;
        updateClient.setOnAction(e -> updateClient());
    }

    private void updateClient() {

      if(validateFields()) {
          PutAddressDTO addressDTO = new PutAddressDTO(
                  currentProject.getCustomer().getAddress().getId(),
                  clientAddress.getText(),
                  clientCityField.getText(),
                  clientPhoneField.getText()
          );

          PutCustomerDTO customerDTO = new PutCustomerDTO(
                  currentProject.getCustomer().getId(),
                  clientNameField.getText(),
                  clientEmailField.getText(),
                  clientPhoneField.getText(),
                  clientTypeChooseField.getSelectionModel().getSelectedItem().toString(),
                  addressDTO
          );

          Customer updatedCustomer = projectModel.updateCustomer(customerDTO);
          if (updatedCustomer != null) {
              displayNotification(true,"Customer updated successfully");
              currentProject.setCustomer(updatedCustomer);
          }
      }

    }

    private boolean validateFields() {
        boolean isValid = true;
        List<FormField> fieldsToValidate = Arrays.asList(
                new FormField(clientNameField, "Client name is required"),
                new FormField(clientEmailField, "Client email is required", this::isValidEmail, "Invalid email format"),
                new FormField(clientTypeChooseField, "Client type is required"),
                new FormField(clientPhoneField, "Client phone is required", this::isValidPhone, "Invalid phone number format"),
                new FormField(clientCityField, "Client city is required")
                );

        for (FormField field : fieldsToValidate) {
            if (field.getText().isEmpty()) {
                displayNotification(false,field.getErrorMessage());
                isValid = false;
            } else if (field.getValidationFunction() != null && !field.getValidationFunction().validate(field.getText())) {
                displayNotification(false,field.getErrorMessage());
                isValid = false;
            }
        }

        if (clientTypeChooseField.getSelectionModel().isEmpty()) {
            displayNotification(false,"Client type required");
            isValid = false;
        }

        return isValid;

    }

    private boolean isValidEmail(String email) {
        return true;
    }

    private boolean isValidPhone(String phone) {
        return true;
    }

    private void updateNotes() {
        if(!editorContent.get().isEmpty()){
           String content = tryUpdatedNotes();
           if(!content.isEmpty()){
               editorContent.set(content);
               displayNotification(true,"Notes updated successfully");
              }else{
               displayNotification(false,"Something went wrong, please try again later");
           }
        }
    }

    private String tryUpdatedNotes() {
        try {
            return projectModel.updateNotes(currentProject.getId(), editorContent.get().trim());
        } catch (Exception e) {
            displayNotification(false,"Something went wrong, please try again later");
            return "";
        }
    }
    // if router here set all the info
    public void setCurrentProject(Project project) {
        currentProject = project;
        projectNameField.setText(currentProject.getName());

        CKEditorPane editorPane = new CKEditorPane();
        editorPane.setContent(currentProject.getDescription());

        editorPane.editorContentProperty().addListener((observable, oldValue, newValue) -> {
            editorContent.set(newValue);
        });

        editorBox.getChildren().add(editorPane);

        clientNameField.setText(currentProject.getCustomer().getName());
        clientEmailField.setText(currentProject.getCustomer().getEmail());

        ObservableList<String> options = FXCollections.observableArrayList("PRIVATE", "BUSINESS");
        clientTypeChooseField.setItems(options);
        clientTypeChooseField.setValue(currentProject.getCustomer().getType());

        clientPhoneField.setText(currentProject.getCustomer().getPhoneNumber());
        clientCityField.setText(currentProject.getCustomer().getAddress().getCity());

        clientAddress.setText(currentProject.getCustomer().getAddress().getStreet());


        project.getDevices().forEach(device -> {
            Label deviceTypeName = new Label(device.getName());
            deviceTypeName.setStyle("-fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-min-width: 150px; -fx-max-width: 150px;");

            Label deviceTypeLabel = new Label(device.getDeviceType().toLowerCase());
            deviceTypeLabel.setStyle("-fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-min-width: 80px; -fx-max-width: 80px;");

            Button editButton = new Button("Edit");
            editButton.setStyle("-fx-min-width: 82px; -fx-max-width: 82px;-fx-background-color: #eae9e9");
            editButton.setOnAction(event -> {
                openDeviceWindow(device);
            });

            // Create an HBox for the device details
            HBox deviceDetails = new HBox(deviceTypeName, deviceTypeLabel,editButton);
            deviceDetails.setSpacing(10);
            deviceDetails.setStyle("-fx-min-height: 20px; -fx-alignment: CENTER_LEFT;");

            // Add the device details HBox to the list
            deviceDetailsList.add(deviceDetails);
        });



        VBox scrollPaneContent = new VBox();
        scrollPaneContent.setSpacing(10);
        scrollPaneContent.getChildren().addAll(deviceDetailsList);
        deviceForProjectList.setContent(scrollPaneContent);


        AtomicInteger nonMainImageCounter = new AtomicInteger(0);

        project.getProjectImages().forEach(image -> {
            if (image.isMainImage()) {
                selectedImage.setImage(ImageCache.getImage(image.getId()));
                mainImage = ImageCache.getImage(image.getId());
            } else {
                int nonMainImageIndex = nonMainImageCounter.getAndIncrement();
                if (nonMainImageIndex == 0) {
                    firstUploadedImage.setImage(ImageCache.getImage(image.getId()));
                    expand1.setOnAction(e -> previewImage(firstUploadedImage.getImage()));
                } else if (nonMainImageIndex == 1) {
                    secondUploadedImage.setImage(ImageCache.getImage(image.getId()));
                    expand2.setOnAction(e -> previewImage(secondUploadedImage.getImage()));
                }
            }
        });
    }

    private void displayNotification(boolean isSuccess,String message){
        errorLabel.setText(message);
        if(isSuccess){
            AnimationUtil.animateInOut(notificationPane,4, CustomColor.SUCCESS);
          notificationImage.setImage(new Image(IConType.SUCCESS.getStyle()));
        }else {
            AnimationUtil.animateInOut(notificationPane,4, CustomColor.WARNING);
        }
    }



    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        // select only jpeg and png files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        // show open file dialog<<
        File selectedFile = fileChooser.showOpenDialog(getStage());
        if(selectedFile != null) {
            // set image fit to width and height
            selectedImage.setPreserveRatio(true);
            selectedImage.setFitHeight(600);
            selectedImage.setFitHeight(500);
            selectedImageFile = selectedFile;
            selectedImage.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
            changeSelectedFileHBox();
        }
    }

    private void openDeviceWindow(Device device) {
        try {
            RootController rootController = controllerFactory.loadFxmlFile(ViewType.DEVICE_CRUD);
            Stage stage = new Stage();
            Scene scene = new Scene(rootController.getView());

            stage.initOwner(getStage());
            stage.setTitle("Create new device");

            EventType eventType = EventType.SET_CURRENT_DEVICE;
            CustomEvent event = new CustomEvent(eventType, device, "");
            eventBus.post(event);

            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void changeSelectedFileHBox() {
        newFileUploadBox.setVisible(true);
        fileName.setText(selectedImageFile.getName());
        uploadBtn.setOnAction(e -> uploadFile());
        cancelBtn.setOnAction(e -> cancelUpload());
    }

    private void uploadFile() {
        int mainImageId = currentProject.getProjectImages().stream().filter(CustomImage::isMainImage).findFirst().get().getId();

        Task<Image> task = new Task<Image>() {
            @Override
            public Image call() throws Exception {
                return projectModel.reuploadImage(currentProject.getId(), mainImageId, selectedImageFile);
            }
        };

        task.setOnSucceeded(event -> {
            mainImage = task.getValue();
            displayNotification(true,"Image re-upload successfully");
            cancelUpload();
        });

        task.setOnFailed(event -> {
            displayNotification(false, "Image re-upload failed !");
        });

        new Thread(task).start();
    }
    private void cancelUpload() {
        newFileUploadBox.setVisible(false);
        fileName.setText("");
        selectedImageFile = null;
        selectedImage.setImage(mainImage);
    }



    private void previewImage(Image image ) {
        // open new scene with image inside
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Preview");
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(600);
        imageView.setFitHeight(500);

        VBox layout = new VBox(10, imageView);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
