package com.wuav.client.gui.controllers;

import com.google.inject.Inject;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.models.user.IUserModel;
import com.wuav.client.gui.utils.AlertHelper;
import com.wuav.client.gui.utils.validations.FormField;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UserProfileController  extends RootController implements Initializable {

    @FXML
    private MFXTextField userNameField,userEmail;
    @FXML

    private MFXPasswordField userPsw;
    @FXML

    private MFXButton updateAcc;
    @FXML

    private Label userRole,emailHeader,userName,userCreated;
    @FXML
    private Circle avatar;
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
           emailHeader.setText(userEmail.getText());

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

        List<FormField> fieldsToValidate = Arrays.asList(
                new FormField(userNameField, "User name is required!"),
                new FormField(userEmail, "Email is required" )
        );

        for (FormField field : fieldsToValidate) {
            if (field.getText().isEmpty()) {
                AlertHelper.showDefaultAlert(field.getErrorMessage(), Alert.AlertType.WARNING);
                isValid = false;
            }
        }
        return isValid;

    }

    private void setUpProfilePage() {
        AppUser loggedUser = CurrentUser.getInstance().getLoggedUser();

        avatar.setFill(new ImagePattern(tempImage));
        userName.setText(loggedUser.getName());

        userNameField.setText(loggedUser.getName());
        userEmail.setText(loggedUser.getEmail());
        userPsw.setDisable(true);
        userRole.setText(loggedUser.getRoles().get(0).getName());
        userCreated.setText(loggedUser.getCreatedAt().toString());
        emailHeader.setText(loggedUser.getEmail());
    }
}
