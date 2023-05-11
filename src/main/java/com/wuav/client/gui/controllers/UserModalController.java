package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.models.user.IUserModel;
import com.wuav.client.gui.utils.AlertHelper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextInputControl;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UserModalController  extends RootController implements Initializable {
    @FXML
    private MFXTextField userNameField;
    @FXML
    private MFXTextField userEmailField;
    @FXML
    private ChoiceBox roleField;
    @FXML
    private MFXButton recoverPassword;
    @FXML
    private MFXButton createUserBtn;

    private final IUserModel userModel;
    private final EventBus eventBus;


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
        roleField.getItems().add("ADMIN");
        roleField.getItems().add("MANAGER");
        roleField.getItems().add("SALES");
        roleField.getItems().add("TECHNICIAN");

        roleField.getSelectionModel().select(3); // selects technician by default
    }

    private void createNewUser() {
        if(validateInput()){

          int result = userModel.createUser(
                    userNameField.getText(),
                    userEmailField.getText(),
                    roleField.getSelectionModel().getSelectedItem().toString()
            );
          if(result == 1){
              AlertHelper.showDefaultAlert("User created successfully", Alert.AlertType.INFORMATION);
              eventBus.post(new RefreshEvent(EventType.UPDATE_USER_TABLE));
              getStage().close();

            }else{
              AlertHelper.showDefaultAlert("User creation failed", Alert.AlertType.ERROR);
          }

          // validate result refresh table by sending emit
        }
    }



    private boolean validateInput() {
        boolean isValid = true;

        List<UserModalController.FormField> fieldsToValidate = Arrays.asList(
                new UserModalController.FormField(userNameField, "User name is required!"),
                new UserModalController.FormField(userEmailField, "Email is required" ),
                new UserModalController.FormField(roleField, "Role is required")
        );

        for (UserModalController.FormField field : fieldsToValidate) {
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


    @FunctionalInterface
    private interface ValidationFunction {
        boolean validate();
    }

    private static class FormField {
        private final Node control;
        private final String errorMessage;
        private final UserModalController.ValidationFunction validationFunction;
        private final String errorValidationMessage;

        public FormField(Node control, String errorMessage) {
            this(control, errorMessage, null, null);
        }

        public FormField(Node control, String errorMessage, UserModalController.ValidationFunction validationFunction, String errorValidationMessage) {
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

        public UserModalController.ValidationFunction getValidationFunction() {
            return validationFunction;
        }

        public String getErrorValidationMessage() {
            return errorValidationMessage;
        }
    }

}
