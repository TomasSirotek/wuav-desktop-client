package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.models.user.IUserModel;
import com.wuav.client.gui.utils.AlertHelper;
import com.wuav.client.gui.utils.event.CustomEvent;
import com.wuav.client.gui.utils.validations.FormField;
import com.wuav.client.gui.utils.enums.UserRoleType;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The class UserModalController.
 */
public class UserModalController extends RootController implements Initializable {
    @FXML
    private MFXTextField userNameField, userEmailField;
    @FXML
    private ChoiceBox roleField;
    @FXML
    private MFXButton createUserBtn;
    @FXML
    private MFXProgressSpinner loadSpinner;
    private final IUserModel userModel;
    private final EventBus eventBus;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    /**
     * Instantiates a new User modal controller.
     *
     * @param userModel the user model
     * @param eventBus  the event bus
     */
    @Inject
    public UserModalController(IUserModel userModel, EventBus eventBus) {
        this.userModel = userModel;
        this.eventBus = eventBus;
    }

    /**
     * Initialize.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillClientTypeChooseField();
        createUserBtn.setOnAction(e -> createNewUser());
    }

    private void fillClientTypeChooseField() {
        // fill client type with two option values
        Arrays.stream(UserRoleType.values())
                .toList()
                .forEach(role -> roleField.getItems()
                        .add(role.toString()));

        roleField.getSelectionModel().select(3); // selects technician by default
    }

    // This method is used to create a new user.
    private void createNewUser() {

        // The spinner indicating a loading process is made visible.
        loadSpinner.setVisible(true);

        // A check is made to ensure the input is valid.
        if (validateInput()) {

            // A new task is submitted to the executor service. This task runs in a background thread and does not interfere with the UI thread.
            // Returns immediately after submitting the task and does not wait for the task to complete.
            executorService.submit(() -> {

                // An attempt is made to create a new user using the provided input.
                boolean result = userModel.createUser(
                        userNameField.getText(),
                        userEmailField.getText(),
                        roleField.getSelectionModel().getSelectedItem().toString()
                );

                // Platform.runLater is a method used to ensure that the Runnable inside will be executed on the JavaFX Application Thread. Thread safety.
                Platform.runLater(() -> {
                    if (result) { // The user creation was successful.

                        // The spinner is made invisible, indicating the end of the loading process.
                        loadSpinner.setVisible(false);

                        // A refresh event is posted to the EventBus to update the user table.
                        eventBus.post(new RefreshEvent(EventType.UPDATE_USER_TABLE));

                        // A custom event is posted to the EventBus to show a notification indicating the success of the user creation.
                        eventBus.post(new CustomEvent(EventType.SHOW_NOTIFICATION, true, "User created successfully"));

                        // The current stage is closed.
                        getStage().close();

                        // The executor service is shut down.
                        executorService.shutdown();

                    } else { // The user creation was unsuccessful.

                        // A custom event is posted to the EventBus to show a notification indicating the failure of the user creation.
                        eventBus.post(new CustomEvent(EventType.SHOW_NOTIFICATION, false, "User could not have been created successfully"));
                    }
                });
            });
        }
    }


    private boolean validateInput() {
        boolean isValid = true;

        List<FormField> fieldsToValidate = Arrays.asList(
                new FormField(userNameField, "User name is required!"),
                new FormField(userEmailField, "Email is required"),
                new FormField(roleField, "Role is required")
        );

        for (FormField field : fieldsToValidate) {
            if (field.getText().isEmpty()) {
                AlertHelper.showDefaultAlert(field.getErrorMessage(), Alert.AlertType.WARNING);
                isValid = false;
            }
        }

        if (roleField.getSelectionModel().isEmpty()) {
            AlertHelper.showDefaultAlert("Role is required", Alert.AlertType.WARNING);
            isValid = false;
        }
        return isValid;

    }
}
