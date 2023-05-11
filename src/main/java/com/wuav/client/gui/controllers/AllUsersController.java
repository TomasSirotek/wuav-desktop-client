package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.models.user.IUserModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class AllUsersController  extends RootController implements Initializable {
    @FXML
    private MFXButton createNewUser;
    @FXML
    private TableView<AppUser> userTable;
    @FXML
    private AnchorPane userAnchorPane;
    @FXML
    private TableColumn<AppUser,String> colName;
    @FXML
    private TableColumn<AppUser,String> colEmail;
    @FXML
    private TableColumn<AppUser,String> colRole;
    @FXML
    private TableColumn<AppUser,String> colDate;
    @FXML
    private TableColumn<AppUser,Button> colEdit;
    @FXML
    private TableColumn<AppUser,Button> colDelete;

    private final IUserModel userModel;

    private final IControllerFactory controllerFactory;

    private final EventBus eventBus;

    private boolean isSettings = false;


    @Inject
    public AllUsersController(IUserModel userModel, IControllerFactory controllerFactory, EventBus eventBus) {
        this.userModel = userModel;
        this.controllerFactory = controllerFactory;
        this.eventBus = eventBus;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTable();
        eventBus.register(this);
        createNewUser.setOnAction(e -> openCreateUserWindow("Create new user",ViewType.USER_MODAL, null));

    }


    private void openCreateUserWindow(String title, ViewType viewType, AppUser value) {
        Scene scene = userAnchorPane.getScene();
        Window window = scene.getWindow();
        if (window instanceof Stage) {
            Pane layoutPane = (Pane) scene.lookup("#layoutPane");
            if (layoutPane != null) {
                layoutPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");
                layoutPane.setDisable(true);
                layoutPane.setVisible(true);

                RootController controller = tryToLoadView(viewType);

                if(this.isSettings && value != null){
                    UserSettingsController userSettingsController = (UserSettingsController) controller;
                    userSettingsController.setUserSettings(value);
                }
                show(controller.getView(), title,scene);

            } else {
                System.out.println("AnchorPane not found");
            }
        }
    }

    private void show(Parent parent, String title, Scene previousScene) {
        Stage stage = new Stage();
        Scene scene = new Scene(parent);

        stage.initOwner(getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setOnCloseRequest(e -> {
            Pane layoutPane = (Pane) previousScene.lookup("#layoutPane");
            if (layoutPane != null) {
                layoutPane.setVisible(true);
                layoutPane.setDisable(true);
                layoutPane.setStyle("-fx-background-color: transparent;");
            }
        });
        // set on showing event to know about the previous stage so that it can be accessed from modalAciton controlelr
        stage.setOnShowing(e -> {
            Stage previousStage = (Stage) previousScene.getWindow();
            stage.getProperties().put("previousStage", previousStage);
        });


        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }



    private RootController tryToLoadView(ViewType viewType) {
        try {
            return loadNodesView(viewType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void fillTable() {
        // name
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        // email
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        // role
       colRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoles().get(0).getName()));
        // created date
        colDate.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getCreatedAt();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMMM dd yyyy");
            String formattedDate = dateFormat.format(date);

            return new SimpleStringProperty(date == null ? "No data" : formattedDate);
        });

        colEdit.setCellValueFactory(col -> {
            MFXButton playButton = new MFXButton("");
            //  playButton.getStyleClass().add("success");
            playButton.setPrefWidth(100);
            playButton.setPrefHeight(20);
            var imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/edit.png")));
            imageIcon.setFitHeight(15);
            imageIcon.setFitWidth(15);
            playButton.setGraphic(imageIcon);
            playButton.setOnAction(e -> {
               // sout e
                // open modal window with the inputed data from the table
                this.isSettings = true;
                openCreateUserWindow("User settings",ViewType.USER_SETTINGS, col.getValue());
            });
            return new SimpleObjectProperty<>(playButton);
        });

        colDelete.setCellValueFactory(project -> {
            MFXButton playButton2 = new MFXButton("");
            //  playButton.getStyleClass().add("success");
            playButton2.setPrefWidth(100);
            playButton2.setPrefHeight(20);
            var imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/delete.png")));
            imageIcon.setFitHeight(15);
            imageIcon.setFitWidth(15);
            playButton2.setGraphic(imageIcon);

            playButton2.setOnAction(e -> {
                System.out.println("delete project" + project.getValue());

            });
            return new SimpleObjectProperty<>(playButton2);
        });

        setTableWithUsers();


    }

    /**
     * Registering events
     */
    @Subscribe
    public void handleRefresh(RefreshEvent event) {
        if (event.eventType() == EventType.UPDATE_USER_TABLE) {

            userTable.getItems().clear();
            // Refresh the table with the updated projects list
            userTable.setItems(userModel.getAllUsers());

            Scene scene = userAnchorPane.getScene();
            Window window = scene.getWindow();
            if (window instanceof Stage) {
                Pane layoutPane = (Pane) scene.lookup("#layoutPane");
                if (layoutPane != null) {
                    layoutPane.setStyle("-fx-background-color: transparent");
                    layoutPane.setDisable(false);
                    layoutPane.setVisible(false);
                } else {
                    System.out.println("Could not refresh the table");
                }
            }

        }
    }


    private void setTableWithUsers() {
        userTable.setItems(userModel.getAllUsers());
    }
}
