package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.models.user.IUserModel;
import com.wuav.client.gui.utils.AlertHelper;
import com.wuav.client.gui.utils.enums.ClientType;
import com.wuav.client.gui.utils.enums.UserRoleType;
import com.wuav.client.gui.utils.event.CustomEvent;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserSettingsController extends RootController implements Initializable{

    @FXML
    private MFXButton recoverPassword,updateUserRole;
    @FXML
    private Label emailConfirmLabel,userEmailField;
    @FXML
    private MFXProgressSpinner emailLoad;
    @FXML
    private ChoiceBox roleField;
    private AppUser appUser;

    private final IUserModel userModel;

    private final EventBus eventBus;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        // check if selected model is not equal to the current role
        if (!appUser.getRoles().get(0).getName().equals(roleField.getSelectionModel().getSelectedItem())) {
            // update user role
            appUser.getRoles().get(0).setName(roleField.getSelectionModel().getSelectedItem().toString());

            // Update user in database in a separate thread
            executorService.submit(() -> {
                boolean result = userModel.updateUserRole(appUser.getId(), appUser.getRoles().get(0).getName());

                Platform.runLater(() -> {
                    if(result){
                        eventBus.post(new RefreshEvent(EventType.UPDATE_USER_TABLE));
                        eventBus.post(new CustomEvent(EventType.SHOW_NOTIFICATION, true, "User updated successfully"));
                        getStage().close();
                        executorService.shutdown();
                    }else {
                        getStage().close();
                        eventBus.post(new CustomEvent(EventType.SHOW_NOTIFICATION, false, "User could not update user"));
                    }
                });
            });
        } else {
            // show alert that user role is already the same
            AlertHelper.showDefaultAlert("User role is already the same", Alert.AlertType.WARNING);
        }
    }
    public void setUserSettings(AppUser value) {
        this.appUser = value;
        userEmailField.setText(value.getEmail());

        Arrays.stream(UserRoleType.values())
                .map(Enum::toString)
                .forEach(roleField.getItems()::add);
        // set selection to model value where value.getRole(1) is the role of the user
        roleField.getSelectionModel().select(value.getRoles().get(0).getName());
    }

    private void recoverPasswordByEmail() {
        // do this in parallel and set loading indicator
        emailLoad.setVisible(true);
        executorService.submit(() -> {
            boolean isEmailSent;
            try {
                isEmailSent = userModel.sendRecoveryEmail(appUser.getEmail());
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(() -> {
                if (isEmailSent) {
                    // set loading false
                    emailConfirmLabel.setVisible(true);
                    emailLoad.setVisible(false);
                    executorService.shutdown();
                }
            });
        });
    }
}
