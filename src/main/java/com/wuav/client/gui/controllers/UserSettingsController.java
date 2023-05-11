package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.utilities.email.EmailConnectionFactory;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.models.user.IUserModel;
import com.wuav.client.gui.utils.AlertHelper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import javax.mail.Session;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UserSettingsController extends RootController implements Initializable{

    @FXML
    private MFXButton recoverPassword;
    @FXML
    private Label emailConfirmLabel;
    @FXML
    private MFXProgressSpinner emailLoad;
    @FXML
    private ChoiceBox roleField;
    @FXML
    private Label userEmailField;

    @FXML
    private MFXButton updateUserRole;

    private AppUser appUser;

    private final IUserModel userModel;

    private final EventBus eventBus;

    @Inject
    public UserSettingsController(IUserModel userModel, EventBus eventBus) {
        this.userModel = userModel;
        this.eventBus = eventBus;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateUserRole.setOnAction(e -> updateUserRole());
        recoverPassword.setOnAction(e -> recoverPasswordByEmail());
    }

    private void updateUserRole() {
        // check if selected model is not equal the
        if (appUser.getRoles().get(0).getName() != roleField.getSelectionModel().getSelectedItem()) {
            // update user role
            appUser.getRoles().get(0).setName(roleField.getSelectionModel().getSelectedItem().toString());
            // update user in database
            boolean result = userModel.updateUserRole(appUser.getId(), appUser.getRoles().get(0).getName());
            if(result){
                eventBus.post(new RefreshEvent(EventType.UPDATE_USER_TABLE));
                getStage().close();

            }else {
                AlertHelper.showDefaultAlert("User role could not be updated", Alert.AlertType.ERROR);
            }
        }else {
            // show alert that user role is already the same
            AlertHelper.showDefaultAlert("User role is already the same", Alert.AlertType.WARNING);
        }
    }

    public void setUserSettings(AppUser value) {
        this.appUser = value;
        userEmailField.setText(value.getEmail());

        roleField.getItems().add("ADMIN");
        roleField.getItems().add("MANAGER");
        roleField.getItems().add("SALES");
        roleField.getItems().add("TECHNICIAN");

        // set selection to model value where value.getRole(1) is the role of the user
        roleField.getSelectionModel().select(value.getRoles().get(0).getName());
    }

    private void recoverPasswordByEmail() {

        // do this in parallel and set laoding indicator

        emailLoad.setVisible(true);
        new Thread(() -> {
                try {
                    // set loading to true
                    boolean isEmailSent = userModel.sendRecoveryEmail(appUser.getEmail());

                    Platform.runLater(() -> {
                        // Display message
                        if (isEmailSent) {
                            // set loading false
                            emailConfirmLabel.setVisible(true);
                            emailLoad.setVisible(false);
                            AlertHelper.showDefaultAlert("Email successfully sent with new password ", Alert.AlertType.INFORMATION);

                        }
                    });
                } catch (Exception e) {
                    // Handle sending failure
                    // Show an error message
                    emailLoad.setVisible(false);
                    AlertHelper.showDefaultAlert("Email sending failed " + e.getMessage(), Alert.AlertType.ERROR);

                }
            }).start();
    }




}
