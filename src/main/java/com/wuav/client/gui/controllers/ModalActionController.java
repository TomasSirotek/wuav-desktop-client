package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.be.device.Device;
import com.wuav.client.be.device.MockDevices;
import com.wuav.client.be.device.Projector;
import com.wuav.client.be.device.Speaker;
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
import com.wuav.client.gui.utils.CKEditorPane;
import com.wuav.client.gui.utils.enums.DeviceType;
import com.wuav.client.gui.utils.validations.FormField;
import com.wuav.client.gui.utils.api.ImageOperationFacade;
import com.wuav.client.gui.utils.enums.ClientType;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;
import java.net.URL;
import java.util.*;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class ModalActionController extends RootController implements Initializable {

    @FXML
    private HBox noDeviceSelectedLabel;
    @FXML
    private GridPane imagesPaneFinal2 = new GridPane();
    @FXML
    private Pane imagesPaneFinal,addedFilePane;
    @FXML
    private MFXProgressSpinner uploadProgress;
    @FXML
    private Label uploadTextProgress,imageText;
    @FXML
    private ImageView fetchedImage,qrCodee;
    @FXML
    private ChoiceBox clientTypeChooseField,deviceTypeChooseField;

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
    private MFXButton continueBtn,selectFile,backBtn,createNewProject;
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

    private Timeline imageFetchTimeline;
    private int currentRow = 0;
    private int currentColumn = 0;
    private List<Device> selectedDevices = new ArrayList<>();
    private List<ImageDTO> listOfUploadImages = new ArrayList<>();
    private StringProperty editorContent = new SimpleStringProperty();
    private List<Image> imagesFromApp;
    private Image defaultImage = new Image("/no_data.png");
    private Image fileImage = new Image("/image.png");
    private final IControllerFactory controllerFactory;

    private ImageOperationFacade imageOperationFacade;

    private final int CURRENT_USER_ID = CurrentUser.getInstance().getLoggedUser().getId();
    private final int BARCODE_WIDTH = 200;
    private final int BARCODE_HEIGHT = 200;


    private Device selectedDeviceForCreateEdit = null;

    private final DeviceModel deviceModel;


    // MAIN BOX

    // FOR PROJECTOR
    private TextField resolutionField,connectionType,devicePort;
    // FOR SPEAKER
    private TextField power,volume;

    @FXML
    private VBox deviceCRUDBox;

    @FXML
    private MFXButton cancelCRUD,createCRUD,demoChat;


    private List<Device> devices = new ArrayList<>();

    private TextField textField = new TextField();

    private AutoCompletionBinding<Device> autoCompletionBinding = null;

    @Inject
    public ModalActionController(EventBus eventBus, IControllerFactory controllerFactory, IProjectModel projectModel, ICodesEngine codesEngine, DeviceModel deviceModel) {
        this.eventBus = eventBus;
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;
        this.codesEngine = codesEngine;
        this.deviceModel = deviceModel;
    }


    public void handleFetchImages() {
        this.imageOperationFacade = new ImageOperationFacade(CURRENT_USER_ID);
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
                });
            }
        });
    }

    private void addImageToSelectedImageVBox(Image image) {
        HBox uploadedImage = new HBox();
        uploadedImage.setSpacing(10);
        uploadedImage.setStyle("-fx-margin-bottom: 20px;-fx-padding:10 0 10 25");

        // Create and configure ImageView
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);

        // Add ImageView to the VBox
        uploadedImage.getChildren().add(imageView);

        // Create and configure Label
        Label selectedFileName = new Label("image.png");
        selectedFileName.setStyle("-fx-font-weight: bold; -fx-font-family: 'Arial';");

        // Add Label to the VBox
        uploadedImage.getChildren().add(selectedFileName);

        // Add VBox to the GridPane
        imagesPaneFinal2.add(uploadedImage, currentColumn, currentRow);

        // Update the row and column index for the next image
        currentColumn++;
        if (currentColumn >= imagesPaneFinal2.getColumnConstraints().size()) {
            currentColumn = 0;
            currentRow++;
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupEditor();
        selectFile.setOnAction(e -> selectFile());
        demoChat.setOnAction(e -> openDemoChat());
        setupSearchField();
        fillClientTypeChooseField();
        handleProgressSwitch();
        closeStage();

    }

    private void openDemoChat() {
        try {
            RootController rootController = controllerFactory.loadFxmlFile(ViewType.CHAT);
            Stage stage = new Stage();
            Scene scene = new Scene(rootController.getView());

            stage.initOwner(getStage());
            stage.setTitle("Wuav-assistant");
            stage.setOnCloseRequest(e -> {

            });
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    public void setupSearchField() {
        this.devices = deviceModel.getAllDevices();
        autoCompletionBinding = TextFields.bindAutoCompletion(textField, devices);

        autoCompletionBinding.setOnAutoCompleted(event -> {
            // Create a new Label for the selected device and add it to the VBox
            Device selectedDevice = event.getCompletion();

            selectedDevices.add(selectedDevice);
            noDeviceSelectedLabel.setVisible(false);

            HBox deviceContainer = new HBox();
            Label deviceLabel = new Label(selectedDevice.getName());
            Button removeButton = new Button("Remove");

            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> {
                // Fill the fields with the selected device data
                deviceTypeSelection.setVisible(true); // set device
                deviceName.setText(selectedDevice.getName()); // set name
                // set device type pending on instance is it projector or speaker
                deviceTypeChooseField.getSelectionModel().select(selectedDevice.getClass().getSimpleName().toUpperCase());

                if(selectedDevice instanceof Projector){
                    setupProjectorFields();
                    Projector projector = (Projector) selectedDevice;
                    resolutionField.setText(projector.getResolution());
                    connectionType.setText(projector.getConnectionType());
                    devicePort.setText(projector.getDevicePort());
                }
                else if(selectedDevice instanceof Speaker){
                    setupSpeakerFields();
                    Speaker speaker = (Speaker) selectedDevice;
                    power.setText(speaker.getPower());
                    volume.setText(speaker.getVolume());
                }

                deviceCRUDBox = new VBox();
                deviceCRUDBox.setSpacing(10);
                deviceCRUDBox.setPadding(new Insets(10));

                detailsBoxLoad.getChildren().clear();
                detailsBoxLoad.getChildren().add(deviceCRUDBox);
            });

            removeButton.setOnAction(e -> {
                // Remove this device from the VBox and the selectedDevices list
                deviceBox.getChildren().remove(deviceContainer);
                selectedDevices.remove(selectedDevice);

                if(selectedDevices.isEmpty()){
                    noDeviceSelectedLabel.setVisible(true);
                }

            });

            deviceContainer.getChildren().addAll(deviceLabel,editButton, removeButton);
            deviceBox.getChildren().add(deviceContainer);
        });


        fillDeviceTypeChooseField(); // fill be default

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String text = textField.getText();
                textField.clear(); // clear current field

                boolean deviceFound = devices.stream().anyMatch(device -> device.getName().equals(text));
                if (!deviceFound) {

                    deviceTypeChooseField.setOnAction(e -> {
                        String selectedDeviceType = (String) deviceTypeChooseField.getValue();
                        // Load the FXML file based on the selected device type

                        // CONSTRUCT THE VBOX FOR ALL
                        deviceCRUDBox = new VBox();
                        deviceCRUDBox.setSpacing(10);
                        deviceCRUDBox.setPadding(new Insets(10));

                        if (selectedDeviceType.equals(DeviceType.PROJECTOR.name())) {
                            setupProjectorFields();
                            selectedDeviceForCreateEdit = new Projector(0,deviceName.getText(),"PROJECTOR");
                        } else if (selectedDeviceType.equals(DeviceType.SPEAKER.name())) {
                            setupSpeakerFields();
                        }
                        detailsBoxLoad.getChildren().clear();
                        detailsBoxLoad.getChildren().add(deviceCRUDBox);

                    });

                    createCRUD.setOnAction(e -> validateNewOrEditDevice());

                    cancelCRUD.setOnAction(e1 -> {
                        detailsBoxLoad.getChildren().clear();
                        deviceTypeChooseField.setValue(null);
                        deviceTypeSelection.setVisible(false);
                        deviceName.setText("");
                    });
                    deviceName.setText(text);
                    deviceTypeSelection.setVisible(true);
                }
            }
        });
        searchBoxField.getChildren().add(textField);
    }

    private void validateNewOrEditDevice() {
        if (selectedDeviceForCreateEdit instanceof Projector) {
            if (validateDeviceInput(selectedDeviceForCreateEdit, Arrays.asList(resolutionField, connectionType, devicePort))) {
                //
                int generatedId = UniqueIdGenerator.generateUniqueId();
                Device device = new Projector(generatedId, deviceName.getText(), "PROJECTOR");
                ((Projector) device).setResolution(resolutionField.getText());
                ((Projector) device).setConnectionType(connectionType.getText());
                ((Projector) device).setDevicePort(devicePort.getText());
                // send device for creating

                boolean isDeviceCreated = deviceModel.createDevice(device);
                if(isDeviceCreated){
                    this.devices.clear();
                    this.devices = deviceModel.getAllDevices();
                    autoCompletionBinding = TextFields.bindAutoCompletion(textField, devices);
                    deviceTypeSelection.setVisible(false);
                    deviceName.setText("");
                    detailsBoxLoad.getChildren().clear();


                    AlertHelper.showDefaultAlert("Device created successfully", Alert.AlertType.INFORMATION);
                } else {
                    AlertHelper.showDefaultAlert("Device creation failed", Alert.AlertType.ERROR);
                }
            } else {
                AlertHelper.showDefaultAlert("Please fill all the fields", Alert.AlertType.WARNING);
            }
        } else if (selectedDeviceForCreateEdit instanceof Speaker) {
            if (validateDeviceInput(selectedDeviceForCreateEdit, Arrays.asList(power, volume))) {
                System.out.println(selectedDeviceForCreateEdit.getName());
            } else {
                AlertHelper.showDefaultAlert("Please fill all the fields", Alert.AlertType.WARNING);
            }
        }
    }

    private boolean validateDeviceInput(Device device, List<TextField> fields) {
        return fields.stream().noneMatch(field -> field.getText().isEmpty());
    }

    // setting different field for the devices
    private void setupSpeakerFields() {

            power = new TextField();
            power.setPromptText("Power");

            volume = new TextField();
            volume.setPromptText("Volume");

        deviceCRUDBox.getChildren().addAll(power, volume);

    }

    private void setupProjectorFields() {
        resolutionField = new TextField();
        resolutionField.setPromptText("Resolution");

        connectionType = new TextField();
        connectionType.setPromptText("Connection Type");

        devicePort = new TextField();
        devicePort.setPromptText("Device Port");
        deviceCRUDBox.getChildren().addAll(resolutionField, connectionType, devicePort);
    }


    private void setupEditor() {
        CKEditorPane editorPane = new CKEditorPane();
        // Set the editor to the editor box
        editorVbox.getChildren().add(editorPane);
        // Access the editor content
        editorPane.editorContentProperty().addListener((observable, oldValue, newValue) -> {
           editorContent.set(newValue);
        });
    }

    private void fillClientTypeChooseField() {
        Arrays.stream(ClientType.values())
                .map(Enum::toString)
                .forEach(clientTypeChooseField.getItems()::add);
    }

    private void fillDeviceTypeChooseField(){
        Arrays.stream(DeviceType.values())
                .map(Enum::toString)
                .forEach(deviceTypeChooseField.getItems()::add);
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
            imageOperationFacade.removeImagesFromServer();
            Stage stage = getStage();
            if (stage != null) {
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


    @FunctionalInterface
    private interface ValidationFunction {
        boolean validate();
    }

    private boolean validateFirstTab() {
        boolean isValid = true;
        if (projectNameField.getText().isEmpty()) {
            AlertHelper.showDefaultAlert("Project name is required", Alert.AlertType.WARNING);
            isValid = false;
        }
        return isValid;
    }


    private boolean validateSecondTab() {
        // Implement validation logic for the second tab here
        boolean isValid = true;
        if(editorContent.get().isEmpty()|| selectedImageFile == null){
            AlertHelper.showDefaultAlert("Description and Image is required", Alert.AlertType.WARNING);
            isValid = false;
        }

        return isValid;
    }

    private boolean validateThirdTab() {
        boolean isValid = true;
        List<FormField> fieldsToValidate = Arrays.asList(
                new FormField(clientNameField, "Client name is required"),
                new FormField(clientEmailField, "Client email is required" ),
                new FormField(clientTypeChooseField, "Client type is required"),
                new FormField(clientPhoneField, "Client phone is required"),
                new FormField(clientCityField, "Client city is required"),
                new FormField(clientStreetField, "Client street is required"),
                new FormField(clientZipField, "Client zip code is required")
        );

        for (FormField field : fieldsToValidate) {
            if (field.getText().isEmpty()) {
                AlertHelper.showDefaultAlert(field.getErrorMessage(), Alert.AlertType.WARNING);
                isValid = false;
            } else if (field.getValidationFunction() != null && !field.getValidationFunction().validate(field.getText())) {
                AlertHelper.showDefaultAlert(field.getErrorValidationMessage(), Alert.AlertType.WARNING);
                isValid = false;
            }
        }

        if (clientTypeChooseField.getSelectionModel().isEmpty()) {
            AlertHelper.showDefaultAlert("Client type is required", Alert.AlertType.WARNING);
            isValid = false;
        }

        return isValid;

    }

    private boolean validateFourthTab() {
        // Implement validation logic for the fourth tab here
        return true;
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
            selectFile.setDisable(true);
            changeSelectedFileHBox();
        }

    }

    private void changeImageActionHandleBox() {
    //    imageActionHandleBox.getChildren().clear();
        // add new button preview that has png image inside
        MFXButton preview = new MFXButton("Preview");
        preview.getStyleClass().add("mfx-raised");
        preview.setStyle("-fx-background-color: #E84910; -fx-text-fill: #ffffff;");
        preview.setOnAction(e -> previewImage());
       // imageActionHandleBox.getChildren().add(preview);
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
        column2.setPrefWidth(180);
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

        MFXButton removeFile = new MFXButton("X");
        removeFile.getStyleClass().add("mfx-raised");
        removeFile.setStyle("-fx-background-color:  #E84910; -fx-text-fill: #ffffff;");
        removeFile.setOnAction(e -> removeImage());

// Add the nodes to the grid pane
        gridPane.add(imageView, 0, 0); // ImageView in the first column
        gridPane.add(projectNameLabel, 1, 0); // Project Name Label in the second column
        gridPane.add(removeFile, 3, 0); // Remove button in the fourth column

        addedFilePane.getChildren().add(gridPane);

    }

    private void previewImage() {
        // open new scene with image inside
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Authorize yourself");
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);

        ImageView imageView = new ImageView(selectedImage.getImage());
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

    private void removeImage() {
        selectedImage.setImage(null);
        // set image back to the default not data selected no data in resource folder
        selectedImage.setImage(defaultImage);
        // remove action button and set label back to no image uploaded
        addedFilePane.getChildren().clear();
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

        // Construct the Customer object
        CustomerDTO customerDTO = new CustomerDTO(
                customerId,
                clientNameField.getText().trim(),
                clientEmailField.getText().trim(),
                clientPhoneField.getText().trim(),
                clientTypeChooseField.getSelectionModel().getSelectedItem().toString(),
                addressDTO
        );


        // construct new DTO and add to the list 1# main image
        ImageDTO imageToUpload = new ImageDTO();
        imageToUpload.setId(UniqueIdGenerator.generateUniqueId());
        imageToUpload.setFile(selectedImageFile);
        imageToUpload.setMain(true);

        // add all to list of images to upload
        listOfUploadImages.add(imageToUpload);

        // images from app are already added to the list

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

       // for each selected device print their id's

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

                AlertHelper.showDefaultAlert("Project created successfully", Alert.AlertType.INFORMATION);
                eventBus.post(new RefreshEvent(EventType.UPDATE_TABLE));
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
