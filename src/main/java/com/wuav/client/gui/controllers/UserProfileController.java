package com.wuav.client.gui.controllers;

import com.google.inject.Inject;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.models.user.IUserModel;
import com.wuav.client.gui.utils.AlertHelper;
import java.text.SimpleDateFormat;
import com.wuav.client.gui.utils.AnimationUtil;
import com.wuav.client.gui.utils.enums.CustomColor;
import com.wuav.client.gui.utils.validations.FormField;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserProfileController  extends RootController implements Initializable {

    @FXML
    private Pane errorPane;
    @FXML
    private MFXTextField userNameField,userEmail;
    @FXML
    private Label errorLabel;
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
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        if (validateInput()) {
            AppUser updatedUser = new AppUser();
            updatedUser.setId(CurrentUser.getInstance().getLoggedUser().getId());
            updatedUser.setName(userNameField.getText());
            updatedUser.setEmail(userEmail.getText());
            emailHeader.setText(userEmail.getText());

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Boolean> updateTask = executorService.submit(() -> userModel.updateUserById(updatedUser));

            executorService.shutdown();

            Platform.runLater(() -> {
                try {
                    boolean updateResult = updateTask.get();
                    if (updateResult) {
                        AnimationUtil.animateInOut(errorPane, 4, CustomColor.INFO);
                        errorLabel.setText("Profile updated successfully");
                        setUpProfilePage();
                    } else {
                        AnimationUtil.animateInOut(errorPane, 4, CustomColor.ERROR);
                        errorLabel.setText("Profile update failed !");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    AnimationUtil.animateInOut(errorPane, 4, CustomColor.ERROR);
                    errorLabel.setText(e.getMessage());
                }
            });
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
                AnimationUtil.animateInOut(errorPane,4, CustomColor.ERROR);
                errorLabel.setText(field.getErrorMessage());
                isValid = false;
            }
        }
        return isValid;

    }

    private void setUpProfilePage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMMM dd yyyy");

        AppUser loggedUser = CurrentUser.getInstance().getLoggedUser();

        avatar.setFill(new ImagePattern(tempImage));
        userName.setText(loggedUser.getName());

        userNameField.setText(loggedUser.getName());
        userEmail.setText(loggedUser.getEmail());
        userPsw.setDisable(true);
        userRole.setText(loggedUser.getRoles().get(0).getName());

        String formattedDate = dateFormat.format(loggedUser.getCreatedAt());
        userCreated.setText(formattedDate);

        emailHeader.setText(loggedUser.getEmail());
    }
}
