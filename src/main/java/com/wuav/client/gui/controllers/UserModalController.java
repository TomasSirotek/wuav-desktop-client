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

public class UserModalController  extends RootController implements Initializable {
    @FXML
    private MFXTextField userNameField,userEmailField;
    @FXML
    private ChoiceBox roleField;
    @FXML
    private MFXButton createUserBtn;

    private final IUserModel userModel;
    private final EventBus eventBus;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Inject
    public UserModalController(IUserModel userModel, EventBus eventBus) {
        this.userModel = userModel;
        this.eventBus = eventBus;
    }

    @FXML

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            // fill form with user data
            fillClientTypeChooseField();
            createUserBtn.setOnAction(e-> createNewUser());
    }

    private void fillClientTypeChooseField() {
        // fill client type with two option values
        Arrays.stream(UserRoleType.values())
                .toList()
                .forEach(role -> roleField.getItems()
                        .add(role.toString()));

        roleField.getSelectionModel().select(3); // selects technician by default
    }

    private void createNewUser() {
        if (validateInput()) {
            executorService.submit(() -> {
                boolean result = userModel.createUser(
                        userNameField.getText(),
                        userEmailField.getText(),
                        roleField.getSelectionModel().getSelectedItem().toString()
                );

                Platform.runLater(() -> {
                    if (result) {
                        eventBus.post(new RefreshEvent(EventType.UPDATE_USER_TABLE));
                        eventBus.post(new CustomEvent(EventType.SHOW_NOTIFICATION, true, "User created successfully"));
                        getStage().close();
                        executorService.shutdown();
                    } else {
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
                new FormField(userEmailField, "Email is required" ),
                new FormField(roleField, "Role is required")
        );

        for (FormField field : fieldsToValidate) {
            if (field.getText().isEmpty()) {
                AlertHelper.showDefaultAlert(field.getErrorMessage(), Alert.AlertType.WARNING);
                isValid = false;
            }
        }

        if (roleField.getSelectionModel().isEmpty() ) {
            AlertHelper.showDefaultAlert("Role is required", Alert.AlertType.WARNING);
            isValid = false;
        }
        return isValid;

    }
}
