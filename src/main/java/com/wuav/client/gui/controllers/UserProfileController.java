package com.wuav.client.gui.controllers;

import com.google.inject.Inject;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.models.user.IUserModel;
import com.wuav.client.gui.utils.AlertHelper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UserProfileController  extends RootController implements Initializable {


    @FXML
    private MFXTextField userNameField;
    @FXML

    private MFXTextField userEmail;
    @FXML

    private MFXPasswordField userPsw;
    @FXML

    private MFXButton updateAcc;
    @FXML

    private Label userRole;
    @FXML

    private Label userCreated;
    @FXML
    private Circle avatar;
    @FXML
    private Label userName;
    private Image tempImage = new Image("diceBar1.png");

    private final IUserModel userModel;

    @Inject
    public UserProfileController(IUserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpProfilePage();
        updateAcc.setOnAction(e -> updateAccount());
    }

    private void updateAccount() {

        // validate if name email or password are the same if there are disable the button

        if(validateInput()){

           AppUser updatedUser = new AppUser();
           updatedUser.setId(CurrentUser.getInstance().getLoggedUser().getId());
           updatedUser.setName(userNameField.getText());
           updatedUser.setEmail(userEmail.getText());
           updatedUser.setPasswordHash(userPsw.getText());

           boolean updateResult = userModel.updateUserById(updatedUser);
           if(updateResult){
               AlertHelper.showDefaultAlert("Profile updated", Alert.AlertType.INFORMATION);
               setUpProfilePage();
           }else {
               AlertHelper.showDefaultAlert("Profile could not be updated", Alert.AlertType.ERROR);
           }

        }

    }

    private boolean validateInput() {
        boolean isValid = true;

        List<UserProfileController.FormField> fieldsToValidate = Arrays.asList(
                new UserProfileController.FormField(userNameField, "User name is required!"),
                new UserProfileController.FormField(userEmail, "Email is required" ),
                new UserProfileController.FormField(userPsw, "Password is required")

        );

        for (UserProfileController.FormField field : fieldsToValidate) {
            if (field.getText().isEmpty()) {
                AlertHelper.showDefaultAlert(field.getErrorMessage(), Alert.AlertType.WARNING);
                isValid = false;
            }
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
        private final UserProfileController.ValidationFunction validationFunction;
        private final String errorValidationMessage;

        public FormField(Node control, String errorMessage) {
            this(control, errorMessage, null, null);
        }

        public FormField(Node control, String errorMessage, UserProfileController.ValidationFunction validationFunction, String errorValidationMessage) {
            this.control = control;
            this.errorMessage = errorMessage;
            this.validationFunction = validationFunction;
            this.errorValidationMessage = errorValidationMessage;
        }

        public String getText() {
            if (control instanceof TextInputControl) {
                return ((TextInputControl) control).getText();
            } else {
                throw new UnsupportedOperationException("Control type not supported: " + control.getClass().getName());
            }
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public UserProfileController.ValidationFunction getValidationFunction() {
            return validationFunction;
        }

        public String getErrorValidationMessage() {
            return errorValidationMessage;
        }
    }








    private void setUpProfilePage() {

        AppUser loggedUser = CurrentUser.getInstance().getLoggedUser();

        avatar.setFill(new ImagePattern(tempImage));
        userName.setText(loggedUser.getName());

        userNameField.setText(loggedUser.getName());
        userEmail.setText(loggedUser.getEmail());
        userPsw.setText(loggedUser.getPasswordHash());
        userRole.setText(loggedUser.getRoles().get(0).getName());
        userCreated.setText(loggedUser.getCreatedAt().toString());


    }
}
