package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.wuav.client.be.CustomerType;
import com.wuav.client.be.Project;
import com.wuav.client.be.device.Device;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import com.wuav.client.bll.utilities.engines.ICodesEngine;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.CustomerDTO;
import com.wuav.client.gui.dto.ImageDTO;
import com.wuav.client.gui.models.DeviceModel;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.AlertHelper;
import com.wuav.client.gui.utils.AnimationUtil;
import com.wuav.client.gui.utils.CKEditorPane;
import com.wuav.client.gui.utils.enums.CustomColor;
import com.wuav.client.gui.utils.event.CustomEvent;
import com.wuav.client.gui.utils.validations.FormField;
import com.wuav.client.gui.utils.api.ImageOperationFacade;
import com.wuav.client.gui.utils.enums.ClientType;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ModalActionController extends RootController implements Initializable {

    @FXML
    private Pane notificationPane;
    @FXML
    private Label errorLabel;
    @FXML
    private MFXScrollPane deviceForProjectList;
    @FXML
    private HBox noDeviceSelectedLabel;
    @FXML
    private GridPane imagesPaneFinal2 = new GridPane();
    @FXML
    private Pane imagesPaneFinal,addedFilePane;
    @FXML
    private MFXProgressSpinner uploadProgress;
    @FXML
    private Label noDeviceLabel;
    @FXML
    private Label uploadTextProgress,imageText,choosenFileName;
    @FXML
    private ImageView fetchedImage,qrCodee;
    @FXML
    private ChoiceBox deviceTypeChooseField,devicesForChooseBox;
    @FXML
    private MFXTextField clientCityField,projectNameField,clientPhoneField,clientNameField,clientEmailField,clientStreetField,clientZipField,deviceName;
    @FXML
    private TextField descriptionField;
    @FXML
    private ImageView selectedImage,selectedImageView;
    @FXML
    private Tab tab1,tab2,tab3,tab4,tab5;
    @FXML
    private HBox searchBoxField, selectedFileHBox,imageContent,imageActionHandleBox,detailsBoxLoad;
    @FXML
    private MFXButton continueBtn,selectFile,backBtn,deviceCrudToggle;
    @FXML
    private Button individualToggle,businessToggle;
    @FXML
    private TabPane tabPaneCreate;
    @FXML
    private VBox editorVbox,deviceBox;
    @FXML
    private AnchorPane modalPane;
    @FXML
    private HBox deviceTypeSelection;

    private final EventBus eventBus;

    private IProjectModel projectModel;
    private File selectedImageFile;

    private ICodesEngine codesEngine;

    private int currentRow = 0;
    private int currentColumn = 0;
    private List<Device> selectedDevices = new ArrayList<>();
    private List<ImageDTO> listOfUploadImages = new ArrayList<>();
    private StringProperty editorContent = new SimpleStringProperty();
    private Image defaultImage = new Image("/imageUpload.png");
    private Image fileImage = new Image("/image.png");
    private final IControllerFactory controllerFactory;


    private final int CURRENT_USER_ID = CurrentUser.getInstance().getLoggedUser().getId();
    private ImageOperationFacade imageOperationFacade = new ImageOperationFacade(CURRENT_USER_ID);
    private final int BARCODE_WIDTH = 200;
    private final int BARCODE_HEIGHT = 200;

    private final int EDITOR_VIEW_HEIGHT = 300;

    private final DeviceModel deviceModel;


    private List<Device> devices = new ArrayList<>();

    private MFXTextField textField = new MFXTextField();

    private boolean individualToggleSelected;
    private boolean businessToggleSelected;


    @Inject
    public ModalActionController(EventBus eventBus, IControllerFactory controllerFactory, IProjectModel projectModel, ICodesEngine codesEngine, DeviceModel deviceModel) {
        this.eventBus = eventBus;
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;
        this.codesEngine = codesEngine;
        this.deviceModel = deviceModel;
    }

    public void handleFetchImages() {
        uploadProgress.setVisible(true);
        uploadTextProgress.setVisible(true);
        imageOperationFacade.startImageFetch(new ImageOperationFacade.ImageFetchCallback() {
            @Override
            public void onImagesFetched(List<Image> images) {
                Platform.runLater(() -> {
                    for (Image image : images) {
                        addImageToSelectedImageVBox(image);
                    }

                    uploadProgress.setVisible(false);
                    uploadTextProgress.setVisible(false);
                    imagesPaneFinal.getChildren().clear();
                    imagesPaneFinal.getChildren().add(imagesPaneFinal2);
                    listOfUploadImages.addAll(imageOperationFacade.getStoredFetchedImages());
                    listOfUploadImages.forEach(System.out::println);
                });
            }
        });
    }

    private void addImageToSelectedImageVBox(Image image) {
        // Create the outer HBox
        HBox uploadedImage = new HBox();
        uploadedImage.setSpacing(10);
        uploadedImage.setStyle("-fx-margin-bottom: 20px;-fx-padding:10 0 10 25");
        uploadedImage.setAlignment(Pos.CENTER_RIGHT);

        // Create the inner HBox for image, label, and expand button
        HBox innerHBox = new HBox();
        innerHBox.setAlignment(Pos.CENTER);
        innerHBox.setSpacing(10);

        // Create and configure ImageView
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);

        // Create and configure Label
        Label selectedFileName = new Label("image.png");
        selectedFileName.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px");
        selectedFileName.setMaxWidth(Double.MAX_VALUE);
        selectedFileName.setAlignment(Pos.CENTER_LEFT);

        // Add left margin to the Label
        HBox.setMargin(selectedFileName, new Insets(0, 10, 0, 0));

        // Create Expand Button
        Button expandButton = new Button("Expand");
        expandButton.setStyle("-fx-background-color: #eae9e9; -fx-text-fill: #000000; -fx-min-width: 82px; -fx-max-width: 82px;");
        expandButton.setOnAction(event -> {
            displayImage(image);
        });

        // Add ImageView and Label to the inner HBox
        innerHBox.getChildren().addAll(imageView, selectedFileName);

        // Add right margin to the inner HBox
        HBox.setMargin(innerHBox, new Insets(0, 100, 0, 0));

        // Add the inner HBox and Expand Button to the outer HBox
        uploadedImage.getChildren().addAll(innerHBox, expandButton);

        // Add the outer HBox to the GridPane
        imagesPaneFinal2.add(uploadedImage, currentColumn, currentRow);

        // Update the row and column index for the next image
        currentColumn++;
        if (currentColumn >= imagesPaneFinal2.getColumnConstraints().size()) {
            currentColumn = 0;
            currentRow++;
        }
    }


    private void displayImage(Image image) {
        previewImage(image);
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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventBus.register(this);
        setupEditor();
        selectFile.setOnAction(e -> selectFile());
        deviceCrudToggle.setOnAction(e -> openDeviceWindow(false,null));
        fillClientTypeChooseField();
        handleProgressSwitch();
        setupSearchField();
        closeStage();
    }


    @Subscribe
    public void handleRefreshDeviceList(RefreshEvent event) {
        if (event.eventType() == EventType.REFRESH_DEVICE_LIST) {
            this.devices = deviceModel.getAllDevices();
            devicesForChooseBox.getItems().clear();
            devicesForChooseBox.setItems(FXCollections.observableArrayList(devices));
            // find the if any devices in the devices list changed their values and refresh it

        }
    }

    private void openDeviceWindow(boolean isEdit, Device device) {
        try {
            RootController rootController = controllerFactory.loadFxmlFile(ViewType.DEVICE_CRUD);
            Stage stage = new Stage();
            Scene scene = new Scene(rootController.getView());

            stage.initOwner(getStage());
            stage.setTitle("Create new device");
            stage.setOnCloseRequest(e -> {
                // FOR NOW
                devicesForChooseBox.getItems().clear();
                devicesForChooseBox.setItems(FXCollections.observableArrayList(devices));
            });

            if(isEdit){
                EventType eventType = EventType.SET_CURRENT_DEVICE;
                CustomEvent event = new CustomEvent(eventType, device, "");
                eventBus.post(event);
            }

            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setupSearchField() {
        this.devices = deviceModel.getAllDevices();
        devicesForChooseBox.setItems(FXCollections.observableArrayList(devices));


        // Set the event handler for device selection
        devicesForChooseBox.setOnAction(event -> {
            Device selectedDevice = (Device) devicesForChooseBox.getValue();
            if (selectedDevice != null) {
                noDeviceLabel.setVisible(false);
                addDeviceToScrollPane(selectedDevice);
                devicesForChooseBox.getSelectionModel().clearSelection(); // Clear the selected device
            }
        });
    }

    private List<HBox> deviceDetailsList = new ArrayList<>();

    private void addDeviceToScrollPane(Device selectedDevice) {
        // if selected device is existing in the list, do not add it again

        boolean deviceExists = false;
        for (Device device : selectedDevices) {
            if (device.getId() == selectedDevice.getId()) {
                deviceExists = true;
                break;
            }
        }

        if (deviceExists) {
            return;
        }

        selectedDevices.add(selectedDevice);

        Label deviceTypeName = new Label(selectedDevice.getName());
        deviceTypeName.setStyle("-fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-min-width: 100px; -fx-max-width: 100px;");

        Label deviceTypeLabel = new Label(selectedDevice.getDeviceType().toLowerCase());
        deviceTypeLabel.setStyle("-fx-font-weight: italic; -fx-font-family: 'Arial'; -fx-min-width: 80px; -fx-max-width: 80px;");

        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-min-width: 82px; -fx-max-width: 82px;-fx-background-color: #eae9e9");
        editButton.setOnAction(event -> {
            openDeviceWindow(true,selectedDevice);
        });

        // Create a button to remove the device
        Button removeButton = new Button("Remove️");
        removeButton.setStyle("-fx-min-width: 82px; -fx-max-width: 82px;-fx-background-color: black; -fx-text-fill: white");
        removeButton.setOnAction(event -> {
            removeDeviceFromScrollPane(selectedDevice);
        });

        // Create an HBox for the device details
        HBox deviceDetails = new HBox(deviceTypeName, deviceTypeLabel,editButton, removeButton);
        deviceDetails.setSpacing(10);
        deviceDetails.setStyle("-fx-min-height: 20px; -fx-alignment: CENTER_LEFT;");

        // Add the device details HBox to the list
        deviceDetailsList.add(deviceDetails);

        // Set the scroll pane content to the updated VBox
        updateScrollPaneContent();
    }

    private void removeDeviceFromScrollPane(Device selectedDevice) {
        HBox deviceToRemove = null;
        for (HBox deviceDetails : deviceDetailsList) {
            Label deviceTypeNameLabel = (Label) deviceDetails.getChildren().get(0);
            String deviceName = deviceTypeNameLabel.getText();
            if (deviceName.equals(selectedDevice.getName())) {
                deviceToRemove = deviceDetails;
                break;
            }
        }

        if (deviceToRemove != null) {
            selectedDevices.remove(selectedDevice);
            deviceDetailsList.remove(deviceToRemove);
            updateScrollPaneContent();
        }
    }

    private void updateScrollPaneContent() {
        VBox scrollPaneContent = new VBox();
        scrollPaneContent.setSpacing(10);
        scrollPaneContent.getChildren().addAll(deviceDetailsList);
        deviceForProjectList.setContent(scrollPaneContent);
    }


    private void setupEditor() {
        CKEditorPane editorPane = new CKEditorPane();
        // Set the editor to the editor box

        editorVbox.getChildren().add(editorPane);
        editorVbox.setPrefHeight(EDITOR_VIEW_HEIGHT);
        editorVbox.setMaxHeight(EDITOR_VIEW_HEIGHT);
        editorVbox.setMinWidth(EDITOR_VIEW_HEIGHT);


        // Access the editor content
        editorPane.editorContentProperty().addListener((observable, oldValue, newValue) -> {
           editorContent.set(newValue);
        });
    }


    private void fillClientTypeChooseField() {
        individualToggle.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        businessToggle.setStyle("-fx-background-color: #eae9e9; -fx-text-fill: black;");

        individualToggle.setOnAction(event -> {
            individualToggleSelected = !individualToggleSelected;
            updateToggleButtonStyle(individualToggle, individualToggleSelected);
            businessToggleSelected = false;
            updateToggleButtonStyle(businessToggle, businessToggleSelected);
            // Store the selected value or perform any other actions
            System.out.println("Individual Toggle Selected: " + individualToggleSelected);
        });

        businessToggle.setOnAction(event -> {
            businessToggleSelected = !businessToggleSelected;
            updateToggleButtonStyle(businessToggle, businessToggleSelected);
            individualToggleSelected = false;
            updateToggleButtonStyle(individualToggle, individualToggleSelected);
            // Store the selected value or perform any other actions
            System.out.println("Business Toggle Selected: " + businessToggleSelected);
        });
    }


    private void updateToggleButtonStyle(Button button, boolean selected) {
        if (selected) {
            button.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        } else {
            button.setStyle("-fx-background-color: #eae9e9; -fx-text-fill: black;");
        }
    }


    private void handleProgressSwitch() {
        Tab[] tabs = new Tab[]{tab1, tab2, tab3, tab4,tab5};
        final int[] currentTab = {0};

        tabs[currentTab[0]].setDisable(false);

        continueBtn.setOnAction(e -> {
            if (currentTab[0] < tabs.length - 1) {
                if (checkTabContent(currentTab[0])) { // Check if the current tab content is valid
                    tabs[currentTab[0]].setDisable(true);
                    currentTab[0]++;
                    tabs[currentTab[0]].setDisable(false);
                    tabPaneCreate.getSelectionModel().selectNext();

                    System.out.println(editorContent.get());

                    if(currentTab[0] == 4) {
                        tryToGenerateQRForApp();
                        handleFetchImages();
                        continueBtn.setText("Finish");
                    }

                    if (currentTab[0] > 0) {
                        backBtn.setVisible(true);
                    } else {
                        backBtn.setVisible(false);
                    }
                }
            } else {
                if (checkTabContent(currentTab[0])) { // Check if the last tab content is valid
                    createNewProject();
                    continueBtn.setText("Finish");
                    imageOperationFacade.stopImageFetch();
                    imageOperationFacade.removeImagesFromServer();
                    closeStage();
                }
            }
        });


        backBtn.setOnAction(e -> {
            if (currentTab[0] > 0) {
                tabs[currentTab[0]].setDisable(true);
                currentTab[0]--;
                tabs[currentTab[0]].setDisable(false);
                tabPaneCreate.getSelectionModel().selectPrevious();

                if (currentTab[0] < tabs.length - 1) {
                    continueBtn.setText("Next");
                }

                if (currentTab[0] > 0) {
                    backBtn.setVisible(true);
                } else {
                    backBtn.setVisible(false);
                }
            }
        });

        if (currentTab[0] == 0) {
            backBtn.setVisible(false);
        }

    }

    private void closeStage() {
        if(this.root != null){
            Stage stage = getStage();
            if (stage != null) {
                imageOperationFacade.removeImagesFromServer();
                imageOperationFacade.stopImageFetch();
                // Trigger the close request
                stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                // Close the stage
                stage.close();
            }
        }

    }


    private boolean checkTabContent(int tabIndex) {
        // use of validation interface to validate the tabs  efficiently
        ValidationFunction[] validationFunctions = new ValidationFunction[]{
                this::validateFirstTab,
                this::validateSecondTab,
                this::validateThirdTab,
                this::validateFourthTab,
                this::validateFifthTab
        };

        return validationFunctions[tabIndex].validate();
    }

    private boolean validateFifthTab() {
        return true;
    }


    private void tryToGenerateQRForApp(){
        try {
            ImageView generatedQRCodeImageView = codesEngine.generateQRCodeImageView(
                    CURRENT_USER_ID,
                    projectNameField.getText(),
                    BARCODE_WIDTH, BARCODE_HEIGHT);
            Image qrCodeImage = generatedQRCodeImageView.getImage();
            qrCodee.setImage(qrCodeImage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void displayError(String message){
        errorLabel.setText(message);
        AnimationUtil.animateInOut(notificationPane,4, CustomColor.WARNING);
    }

    @FunctionalInterface
    private interface ValidationFunction {
        boolean validate();
    }

    private boolean validateFirstTab() {
        boolean isValid = true;
        if (projectNameField.getText().isEmpty()) {
            displayError("Project name is required");
            isValid = false;
        }
        return isValid;
    }


    private boolean validateSecondTab() {
        // Implement validation logic for the second tab here
        boolean isValid = true;
        if(editorContent.get().isEmpty()|| selectedImageFile == null){
            displayError("Please select an image");
            isValid = false;
        }

        return isValid;
    }

    private boolean validateThirdTab() {
        boolean isValid = true;
        List<FormField> fieldsToValidate = Arrays.asList(
                new FormField(clientNameField, "Client name is required"),
                new FormField(clientEmailField, "Client email is required" ),
                new FormField(clientPhoneField, "Client phone is required"),
                new FormField(clientCityField, "Client city is required"),
                new FormField(clientStreetField, "Client street is required"),
                new FormField(clientZipField, "Client zip code is required")
        );

        for (FormField field : fieldsToValidate) {
            if (field.getText().isEmpty()) {
                displayError(field.getErrorMessage());
                isValid = false;
            } else if (field.getValidationFunction() != null && !field.getValidationFunction().validate(field.getText())) {
                displayError(field.getErrorMessage());
                isValid = false;
            }
        }
        return isValid;

    }

    private boolean validateFourthTab() {
        // Implement validation logic for the fourth tab here
        boolean isValid = true;
        if(selectedDevices.isEmpty()){
            displayError("Please select at least one device");
            isValid = false;
        }
        return isValid;
    }


    // END OF VALIDATIONS

    // show transaction erro however that is just apple not liking javaFx and its not a real error
    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        // select only jpeg and png files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        // show open file dialog
        File selectedFile = fileChooser.showOpenDialog(getStage());
        if(selectedFile != null) {
            // set image fit to width and height
            selectedImage.setPreserveRatio(true);
            selectedImage.setFitHeight(600);
            selectedImage.setFitHeight(500);

            selectedImageFile = selectedFile; // SET TO SELECTED IMAGE

            selectedImage.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
            selectedImage.setSmooth(true);

            choosenFileName.setText(truncate(selectedFile.getAbsolutePath(), 30));
            selectFile.setDisable(true);
            changeSelectedFileHBox();
        }

    }

    /**
     * Truncate the string to the specified length and add "..." at the end
     * @param str the string to truncate
     * @param maxLength the maximum length of the string
     * @return
     */

    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    private void changeSelectedFileHBox() {

        imageText.setVisible(false);

        addedFilePane.getChildren().clear();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Set horizontal gap between grid cells
        gridPane.setVgap(10); // Set vertical gap between grid cells
        gridPane.setStyle("-fx-padding: 20 0 0 0 ");

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS); // Allow column to grow horizontally

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPrefWidth(145);
        column2.setHgrow(Priority.ALWAYS); // Allow column to grow horizontally

        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPrefWidth(100);

        column3.setHgrow(Priority.ALWAYS); // Allow column to grow horizontally

        ColumnConstraints column4 = new ColumnConstraints();
        column4.setHgrow(Priority.ALWAYS); // Allow column to grow horizontally

        gridPane.getColumnConstraints().addAll(column1, column2, column3, column4);

        gridPane.setHgrow(gridPane, Priority.ALWAYS); // Make the GridPane fill the available width

        ImageView imageView = new ImageView(fileImage); // Replace `defaultImage` with your image source
        imageView.setFitWidth(30); // Set the desired width for the ImageView
        imageView.setFitHeight(30); // Set the desired height for the ImageView

        Label projectNameLabel = new Label(selectedImageFile.getName());
        projectNameLabel.setStyle("-fx-font-weight: bold; -fx-font-family: 'Arial';");

        MFXButton removeFile = new MFXButton("Remove");
        removeFile.getStyleClass().add("mfx-raised");
        removeFile.setStyle("-fx-background-color:  black; -fx-text-fill: #ffffff;-fx-min-width: 82px; -fx-max-width: 82px;");
        removeFile.setOnAction(e -> removeImage());

// Add the nodes to the grid pane
        gridPane.add(imageView, 0, 0); // ImageView in the first column
        gridPane.add(projectNameLabel, 1, 0); // Project Name Label in the second column
        gridPane.add(removeFile, 3, 0); // Remove button in the fourth column

        addedFilePane.getChildren().add(gridPane);

    }

    private void removeImage() {
        selectedImage.setImage(null);
        // set image back to the default not data selected no data in resource folder
        selectedImage.setImage(defaultImage);
        // remove action button and set label back to no image uploaded
        addedFilePane.getChildren().clear();
        choosenFileName.setText("No file selected");

        selectFile.setDisable(false);
        imageText.setVisible(true);
    }

    private void voidTriggerProjectLoadingStatus(){
        eventBus.post(new RefreshEvent(EventType.START_LOADING_PROJECTS));
    }


    private void createNewProject() {
        voidTriggerProjectLoadingStatus(); // start loading in the project window
        // generating all the ids
        var projectId = UniqueIdGenerator.generateUniqueId();
        var customerId = UniqueIdGenerator.generateUniqueId();
        var addressId = UniqueIdGenerator.generateUniqueId();

        // Construct the Address object
        AddressDTO addressDTO = new AddressDTO(
                addressId,
                clientStreetField.getText().trim(),
                clientCityField.getText().trim(),
                clientZipField.getText().trim()
        );

        // HERE IT HAS TO BE CHANGE DEPENDING ON WHICH CLIENT TOOGLE IS SELECTED
         ClientType clientType = ClientType.PRIVATE;
         if(businessToggleSelected) clientType = ClientType.BUSINESS;
         if(individualToggleSelected) clientType = ClientType.PRIVATE;

        // Construct the Customer object
        CustomerDTO customerDTO = new CustomerDTO(
                customerId,
                clientNameField.getText().trim(),
                clientEmailField.getText().trim(),
                clientPhoneField.getText().trim(),
                clientType.toString(),
                addressDTO
        );


        // construct new DTO and add to the list 1# main image
        ImageDTO imageToUpload = new ImageDTO();
        imageToUpload.setId(UniqueIdGenerator.generateUniqueId());
        imageToUpload.setFile(selectedImageFile);
        imageToUpload.setMain(true);

        // add all to list of images to upload
        listOfUploadImages.add(imageToUpload);

        // collect all info into a project object
        CreateProjectDTO projectToCreate = new CreateProjectDTO(
                projectId,
                projectNameField.getText().trim(),
                editorContent.get().trim(),
                listOfUploadImages,
                customerDTO,
                selectedDevices
        );

        int currentUserId = CurrentUser.getInstance().getLoggedUser().getId();

        Task<Boolean> loadDataTask = new Task<>() {
            @Override
            protected Boolean call() throws IOException {
                return projectModel.createProject(currentUserId, projectToCreate);
            }
        };

        loadDataTask.setOnSucceeded(event -> {
            boolean result = loadDataTask.getValue();

            if (result) {

                Project newProject = projectModel.getProjectById(projectId);

                // Update the cache with the new project
                projectModel.updateCacheForUser(currentUserId, newProject);

                eventBus.post(new RefreshEvent(EventType.UPDATE_TABLE));
                EventType eventType = EventType.SHOW_NOTIFICATION;
                CustomEvent notificationEvent = new CustomEvent(eventType, true, "Project created successfully");
                eventBus.post(notificationEvent);
                runInParallel(ViewType.PROJECTS);
            } else {
                AlertHelper.showDefaultAlert("Error creating project", Alert.AlertType.ERROR);
            }
        });

        new Thread(loadDataTask).start();
    }

    private void runInParallel(ViewType type) {
        final RootController[] parent = {null};
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() throws IOException {
                parent[0] = loadNodesView(type);
                return null;
            }
        };
        loadDataTask.setOnSucceeded(event -> {
            switchToView(parent[0].getView());
        });
        new Thread(loadDataTask).start();
    }

    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }

    private void switchToView(Parent parent) {
        Stage  test = (Stage) getStage().getProperties().get("previousStage");
        Scene previousScene = test.getScene();
        Pane layoutPane = (Pane) previousScene.lookup("#layoutPane");
        StackPane appContent = (StackPane) previousScene.getRoot().lookup("#app_content");
        if (appContent != null || layoutPane != null) {
            getStage().close();
            layoutPane.setDisable(true);
            layoutPane.setStyle("-fx-background-color: transparent;");
            appContent.getChildren().clear();
            appContent.getChildren().add(parent);
        }
    }




}
