package com.wuav.client.gui.controllers;

import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.AlertHelper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.concurrent.Task;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ModalActionController extends RootController implements Initializable {

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
    private MFXTextField clientAddressField;

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

    @Inject
    public ModalActionController(IControllerFactory controllerFactory, IProjectModel projectModel) {
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectFile.setOnAction(e -> selectFile());
        fillClientTypeChooseField();

       handleProgressSwitch();
    }

    private void fillClientTypeChooseField() {
        // fill client type with two option values
        clientTypeChooseField.getItems().add("PRIVATE");
        clientTypeChooseField.getItems().add("BUSINESS");
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

                    if (currentTab[0] > 0) {
                        backBtn.setVisible(true);
                    } else {
                        backBtn.setVisible(false);
                    }
                }
            } else {
                if (checkTabContent(currentTab[0])) { // Check if the last tab content is valid
                    continueBtn.setText("Finish");
                    System.out.println("Finished");
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

    private boolean checkTabContent(int tabIndex) {
        // use of validation interface to validate the tabs  efficiently
        ValidationFunction[] validationFunctions = new ValidationFunction[]{
                this::validateFirstTab,
                this::validateSecondTab,
                this::validateThirdTab,
                this::validateFourthTab
        };

        return validationFunctions[tabIndex].validate();
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
                new FormField(clientAddressField, "Client address is required")
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
        // reach project service and create new project and pass the project name
        // if project is succesfully

        // get current logged user id
       int userId =  CurrentUser.getInstance().getLoggedUser().getId();
       Project project = projectModel.createProjectByName(userId,projectNameField.getText().trim());
         if(project != null){
           //  projectModel.setCurrentProject(project);
             runInParallel(ViewType.PROJECT_ACTIONS);
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
