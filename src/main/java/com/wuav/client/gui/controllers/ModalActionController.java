package com.wuav.client.gui.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Project;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import com.wuav.client.bll.utilities.engines.ICodesEngine;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.CustomerDTO;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.AlertHelper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModalActionController extends RootController implements Initializable {

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
    private MFXTextField descriptionField;
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

    private Image defaultImage = new Image("/no_data.png");

    private final IControllerFactory controllerFactory;

    private IProjectModel projectModel;

    private File selectedImageFile;


    private ImageView selectedImageView;
    @FXML
    private HBox imageActionHandleBox;

    private ICodesEngine codesEngine;

    private Timeline imageFetchTimeline;

    private List<Image> imagesFromApp;

    @Inject
    public ModalActionController(IControllerFactory controllerFactory, IProjectModel projectModel, ICodesEngine codesEngine) {
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;
        this.codesEngine = codesEngine;
    }


    private void startImageFetch(int userId) {
        AtomicBoolean fetched = new AtomicBoolean(false);
        System.out.println("starting image fetch");
        imageFetchTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    if (!fetched.get()) {
                        List<Image> images = fetchImagesFromServer(userId);
                        if (!images.isEmpty()) {
                            imageContent.getChildren().clear(); // Clear the imageContent VBox before adding new images
                            for (Image image : images) {
                                addImageToSelectedImageVBox(image);
                            }
                            fetched.set(true); // Set fetched to true after successfully fetching images
                        }
                    }
                }),
                new KeyFrame(Duration.seconds(5)) // Adjust the duration based on how often you want to poll the server
        );

        imageFetchTimeline.setCycleCount(Animation.INDEFINITE);
        imageFetchTimeline.play();
    }

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
        // Create an HBox to store the ImageView and Label
        HBox uploadedImage = new HBox();

        // Create and configure ImageView
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        // Add ImageView to the HBox
        uploadedImage.getChildren().add(imageView);

        // Create and configure Label
        Label selectedFileName = new Label("image.png");
        selectedFileName.setStyle("-fx-text-fill: black;");

        // Add Label to the HBox
        uploadedImage.getChildren().add(selectedFileName);

        // Add HBox to the imageContent VBox
        imageContent.getChildren().add(uploadedImage);
    }

    private void stopImageFetch() {
        if (imageFetchTimeline != null) {
            imageFetchTimeline.stop();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectFile.setOnAction(e -> selectFile());


//        Platform.runLater(() -> {
//            Stage stage = (Stage) modalPane.getScene().getWindow();
//            stage.setOnCloseRequest(e -> {
//                stopImageFetch();
//                if (imagesFromApp != null) {
//                    removeImagesFromServer(340);
//                    closeStage();
//                } else {
//                    System.out.println("No images to remove");
//                    closeStage();
//                }
//
//            });
//        });

        fillClientTypeChooseField();


        // PROJECT ID SHOULD NOT BE THERE SINCE ITS NOT GENERATED YET
        try {
            ImageView generatedQRCodeImageView = codesEngine.generateQRCodeImageView(340, 40, 200, 200);
            Image qrCodeImage = generatedQRCodeImageView.getImage();
            qrCodee.setImage(qrCodeImage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        handleProgressSwitch();
    }

    private void fillClientTypeChooseField() {
        // fill client type with two option values
        clientTypeChooseField.getItems().add("PRIVATE");
        clientTypeChooseField.getItems().add("BUSINESS");
    }



    private List<Image> fetchImagesFromServer(int userId) {
        imagesFromApp = new ArrayList<>();
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

                for (String base64Image : base64Images) {
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
                    InputStream decodedInputStream = new ByteArrayInputStream(decodedBytes);
                    imagesFromApp.add(new Image(decodedInputStream));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagesFromApp;
    }



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

                    if(currentTab[0] == 3) {
                        startImageFetch(340);
                    }

                    if (currentTab[0] > 0) {
                        backBtn.setVisible(true);
                    } else {
                        backBtn.setVisible(false);
                    }
                }
            } else {

                if (checkTabContent(currentTab[0])) { // Check if the last tab content is valid
                    continueBtn.setText("Finish");
                    createNewProject();
                   // removeImagesFromServer(340); // ADD LATER
                    stopImageFetch();
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
        Stage stage = getStage();
        if (stage != null) {
            // Trigger the close request
            stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            // Close the stage
            stage.close();
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


    @FXML

    public void fetchImageAction(ActionEvent actionEvent) {
        System.out.println("fetching image BUTTON CLICK");
      //  fetchImageFromServer(340);
        startImageFetch(340);
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

    @FunctionalInterface
    private interface FieldValidator {
        boolean validate(String input);
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
            selectedImageFile = selectedFile;
            selectedImage.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
            selectedFileHBox.setVisible(true);
            selectFile.setDisable(true);
            changeImageActionHandleBox();
            changeSelectedFileHBox();


        }

    }

    private void changeImageActionHandleBox() {
        imageActionHandleBox.getChildren().clear();
        // add new button preview that has png image inside
        MFXButton preview = new MFXButton("Preview");
        preview.getStyleClass().add("mfx-raised");
        preview.setStyle("-fx-background-color: #E84910; -fx-text-fill: #ffffff;");
        preview.setOnAction(e -> previewImage());
        imageActionHandleBox.getChildren().add(preview);
    }

    private void changeSelectedFileHBox() {
        selectedFileHBox.getChildren().clear();
        // create hbox with label at the start and button at the end with x as text and make the box light red and so that text start at the start and ubtton at the end
        Label selectedFileLabel = new Label("Selected File: ");
        selectedFileHBox.setStyle("-fx-text-fill: #ffffff;");
        selectedFileHBox.setStyle("-fx-background-color: #E84910; -fx-spacing: 10; -fx-opacity: 0.8; -fx-padding: 10;");
        selectedFileHBox.getChildren().add(selectedFileLabel);
        Label selectedFileName = new Label("image.png");
        selectedFileName.setStyle("-fx-text-fill: black;");
        selectedFileHBox.getChildren().add(selectedFileName);
        MFXButton removeFile = new MFXButton("X");
        removeFile.getStyleClass().add("mfx-raised");
        removeFile.setStyle("-fx-background-color: red; -fx-text-fill: #ffffff;");
        removeFile.setOnAction(e -> removeImage());
        selectedFileHBox.getChildren().add(removeFile);

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

        imageActionHandleBox.getChildren().clear();
        Label noImageUploaded = new Label("No Image Uploaded");
        imageActionHandleBox.getChildren().add(noImageUploaded);
        // clean selected file hbox and set back the text to no file selected
        selectedFileHBox.getChildren().clear();
        Label noFileSelected = new Label("No File Selected");
        selectedFileHBox.getChildren().add(noFileSelected);
        // set back the color back to white
        selectedFileHBox.setStyle("-fx-background-color: #ffffff;");
        selectFile.setDisable(false);
    }



    private void createNewProject() {

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



        // collect all info into a project object
        CreateProjectDTO projectToCreate = new CreateProjectDTO(
                projectId,
                projectNameField.getText().trim(),
                descriptionField.getText().trim(),
                selectedImageFile,
                customerDTO,
                imagesFromApp
        );

        System.out.println(projectToCreate);
        int currentUserId = CurrentUser.getInstance().getLoggedUser().getId();
        boolean result = projectModel.createProject(currentUserId,projectToCreate);
        if(result){
            runInParallel(ViewType.PROJECT_ACTIONS);
        }else {
            AlertHelper.showDefaultAlert("Error creating project", Alert.AlertType.ERROR);
        }

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


    private static class FormField {
        private final Node control;
        private final String errorMessage;
        private final FieldValidator validationFunction;
        private final String errorValidationMessage;

        public FormField(Node control, String errorMessage) {
            this(control, errorMessage, null, null);
        }

        public FormField(Node control, String errorMessage, FieldValidator validationFunction, String errorValidationMessage) {
            this.control = control;
            this.errorMessage = errorMessage;
            this.validationFunction = validationFunction;
            this.errorValidationMessage = errorValidationMessage;
        }

        public String getText() {
            if (control instanceof TextInputControl) {
                return ((TextInputControl) control).getText();
            } else if (control instanceof ChoiceBox) {
                Object value = ((ChoiceBox<?>) control).getValue();
                return value != null ? value.toString() : "";
            } else {
                throw new UnsupportedOperationException("Control type not supported: " + control.getClass().getName());
            }
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public FieldValidator getValidationFunction() {
            return validationFunction;
        }

        public String getErrorValidationMessage() {
            return errorValidationMessage;
        }
    }


}
