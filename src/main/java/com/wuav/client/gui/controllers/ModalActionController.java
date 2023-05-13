package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.wuav.client.be.Project;
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
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.AlertHelper;
import com.wuav.client.gui.utils.CKEditorPane;
import com.wuav.client.gui.utils.FormField;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModalActionController extends RootController implements Initializable {

    @FXML
    private VBox editorVbox;
    @FXML
    private Pane imagesPaneFinal;
    @FXML
    private MFXProgressSpinner uploadProgress;
    @FXML
    private Label uploadTextProgress;
    @FXML
    private Label imageText;
    @FXML
    private Pane addedFilePane;
    @FXML
    private MFXTextField clientZipField;
    @FXML
    private MFXTextField clientStreetField;
    @FXML
    private HBox imageContent;
    @FXML
    private ImageView fetchedImage;
    @FXML
    private ImageView qrCodee;
    @FXML
    private MFXTextField clientNameField;
    @FXML
    private MFXTextField clientEmailField;
    @FXML
    private ChoiceBox clientTypeChooseField;
    @FXML
    private MFXTextField clientPhoneField;
    @FXML
    private MFXTextField clientCityField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ImageView selectedImage;
    @FXML
    private HBox selectedFileHBox;
    @FXML
    private MFXButton selectFile;
    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;
    @FXML
    private Tab tab3;
    @FXML
    private Tab tab4;
    @FXML
    private MFXButton continueBtn;

    @FXML
    private MFXButton backBtn;
    @FXML
    private TabPane tabPaneCreate;
    @FXML
    private MFXTextField projectNameField;
    @FXML
    private AnchorPane modalPane;
    @FXML
    private MFXButton createNewProject;

    private final EventBus eventBus;

    private Image defaultImage = new Image("/no_data.png");
    private Image fileImage = new Image("/image.png");

    private final IControllerFactory controllerFactory;

    private StringProperty editorContent = new SimpleStringProperty();

    private IProjectModel projectModel;

    private File selectedImageFile;


    private List<ImageDTO> listOfUploadImages = new ArrayList<>();

    private ImageView selectedImageView;
    @FXML
    private HBox imageActionHandleBox;

    private ICodesEngine codesEngine;

    private Timeline imageFetchTimeline;

    private List<Image> imagesFromApp;
    @FXML
    private GridPane imagesPaneFinal2 = new GridPane();
    private int currentRow = 0;
    private int currentColumn = 0;

    @Inject
    public ModalActionController(EventBus eventBus, IControllerFactory controllerFactory, IProjectModel projectModel, ICodesEngine codesEngine) {
        this.eventBus = eventBus;
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;
        this.codesEngine = codesEngine;
    }


    private void startImageFetch(int userId) {
        AtomicBoolean fetched = new AtomicBoolean(false);
        System.out.println("starting image fetch");
        // SETTING UI TO INDICATE TO END USER
        uploadProgress.setVisible(true);
        uploadTextProgress.setVisible(true);


        imageFetchTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    if (!fetched.get()) {
                        List<ImageDTO> images = fetchImagesFromServer(userId);
                        if (!images.isEmpty()) {
                        imagesPaneFinal.getChildren().clear();
                            for (ImageDTO imageDTO : images) {
                                addImageToSelectedImageVBox(new Image(imageDTO.getFile().toURI().toString()));

                                listOfUploadImages.add(imageDTO);
                            }
                            fetched.set(true); // Set fetched to true after successfully fetching images
                            // set back once all fetched
                            uploadProgress.setVisible(false);
                            uploadTextProgress.setVisible(false);
                            imagesPaneFinal.getChildren().add(imagesPaneFinal2);

                        }
                    }
                }),
                new KeyFrame(Duration.seconds(5)) // Adjust the duration based on how often you want to poll the server
        );

        imageFetchTimeline.setCycleCount(Animation.INDEFINITE);
        imageFetchTimeline.play();
    }

//    private void startImageFetch(int userId) {
//        AtomicBoolean fetched = new AtomicBoolean(false);
//        System.out.println("starting image fetch");
//        imageFetchTimeline = new Timeline(
//                new KeyFrame(Duration.ZERO, e -> {
//                    if (!fetched.get()) {
//                        List<Image> images = fetchImagesFromServer(userId);
//                        if (!images.isEmpty()) {
//                            imageContent.getChildren().clear(); // Clear the imageContent VBox before adding new images
//                            for (Image image : images) {
//                                addImageToSelectedImageVBox(image);
//                            }
//                            fetched.set(true); // Set fetched to true after successfully fetching images
//                        }
//                    }
//                }),
//                new KeyFrame(Duration.seconds(5)) // Adjust the duration based on how often you want to poll the server
//        );
//
//        imageFetchTimeline.setCycleCount(Animation.INDEFINITE);
//        imageFetchTimeline.play();
//    }

    private void removeImagesFromServer(int userId) {
        try {
            URL url = new URL("http://localhost:5000/api/users/" + userId + "/temp-images");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("Images removed from server");
            } else {
                System.out.println("Error removing images from server: HTTP status code " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private void addImageToSelectedImageVBox(Image image) {
        System.out.println("Adding image to imagesPaneFinal");

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

    private void stopImageFetch() {
        if (imageFetchTimeline != null) {
            imageFetchTimeline.stop();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupEditor();
        selectFile.setOnAction(e -> selectFile());
        fillClientTypeChooseField();
        handleProgressSwitch();
        closeStage();
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
        // fill client type with two option values
        clientTypeChooseField.getItems().add("PRIVATE");
        clientTypeChooseField.getItems().add("BUSINESS");
    }


    private List<ImageDTO> fetchImagesFromServer(int userId) {
       List<ImageDTO> imagesFromApp = new ArrayList<>();
        try {
            URL url = new URL("http://localhost:5000/api/users/" + userId + "/temp-images");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                String jsonResponse = responseBuilder.toString();

                // Deserialize JSON response into a list of strings
                Gson gson = new Gson();
                TypeToken<List<String>> token = new TypeToken<List<String>>() {};
                List<String> base64Images = gson.fromJson(jsonResponse, token.getType());

                int fileIndex = 0;
                for (String base64Image : base64Images) {
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
                    Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "tempImages");
                    Files.createDirectories(tempDir);

                    File tempFile = tempDir.resolve("tempImage" + fileIndex + ".png").toFile();
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        fos.write(decodedBytes);
                    }

                    // Create a new ImageDTO instance and set its properties
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setId(UniqueIdGenerator.generateUniqueId());
                    imageDTO.setFile(tempFile);
                    imageDTO.setMain(false); // You can set this property based on your needs

                    imagesFromApp.add(imageDTO);
                    fileIndex++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagesFromApp;
    }



//    private List<Image> fetchImagesFromServer(int userId) {
//        imagesFromApp = new ArrayList<>();
//        try {
//            URL url = new URL("http://localhost:5000/api/users/" + userId + "/temp-images");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.connect();
//            int responseCode = connection.getResponseCode();
//
//            if (responseCode == 200) {
//                InputStream inputStream = connection.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                StringBuilder responseBuilder = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    responseBuilder.append(line);
//                }
//                String jsonResponse = responseBuilder.toString();
//
//                // Deserialize JSON response into a list of strings
//                Gson gson = new Gson();
//                TypeToken<List<String>> token = new TypeToken<List<String>>() {};
//                List<String> base64Images = gson.fromJson(jsonResponse, token.getType());
//
//                for (String base64Image : base64Images) {
//                    byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
//                    InputStream decodedInputStream = new ByteArrayInputStream(decodedBytes);
//                    imagesFromApp.add(new Image(decodedInputStream));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return imagesFromApp;
//    }





    private void handleProgressSwitch() {
        Tab[] tabs = new Tab[]{tab1, tab2, tab3, tab4}; // Replace with the actual tabs in your TabPane
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

                    if(currentTab[0] == 3) {
                        tryToGenerateQRForApp();
                        startImageFetch(340);
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
                    stopImageFetch();
                    removeImagesFromServer(340); // ADD LATER CURRENT USER ID
                    closeStage();

                    // Perform any additional actions here
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

    public void closeStage() {
        removeImagesFromServer(340); // ADD LATER CURRENT USER ID
        if(this.root != null){
            removeImagesFromServer(340); // ADD LATER CURRENT USER ID
            Stage stage = getStage();
            if (stage != null) {
                removeImagesFromServer(340); // ADD LATER CURRENT USER ID
                stopImageFetch();
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
                this::validateFourthTab
        };

        return validationFunctions[tabIndex].validate();

       // return true;
    }


    private void tryToGenerateQRForApp(){
        try {
            ImageView generatedQRCodeImageView = codesEngine.generateQRCodeImageView(340, projectNameField.getText(), 200, 200);
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
        if(descriptionField.getText().trim().equals("") || selectedImageFile == null){
            AlertHelper.showDefaultAlert("Description and Image is required", Alert.AlertType.WARNING);
            isValid = false;
        }

        return isValid;
    }

    private boolean validateThirdTab() {
        boolean isValid = true;
        List<FormField> fieldsToValidate = Arrays.asList(
                new FormField(clientNameField, "Client name is required"),
                new FormField(clientEmailField, "Client email is required", this::isValidEmail, "Invalid email format"),
                new FormField(clientTypeChooseField, "Client type is required"),
                new FormField(clientPhoneField, "Client phone is required", this::isValidPhone, "Invalid phone number format"),
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

    private boolean isValidEmail(String email) {
        // Implement email validation logic here
        return true;
    }

    private boolean isValidPhone(String phone) {
        // Implement phone number validation logic here
        return true;
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
         //   selectedFileHBox.setVisible(true);
            selectFile.setDisable(true);

           // changeImageActionHandleBox();
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

    private         GridPane gridPane = new GridPane();
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
        // set image back to the defualt not data selected no data in resource folder
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

        // FIRST UPLOADED IMAGE
        // selectedImageFile => File (format=

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
                customerDTO
        );

        System.out.println(projectToCreate.images());
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

    private Parent getParent(Scene scene) {
        Parent parent = scene.getRoot();
        while (parent != null && !(parent instanceof AnchorPane)) {
            parent = parent.getParent();
        }
        return parent;
    }

    //endregion




}
