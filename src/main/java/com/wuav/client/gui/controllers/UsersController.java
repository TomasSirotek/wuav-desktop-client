package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.models.user.IUserModel;
import com.wuav.client.gui.utils.AlertHelper;
import com.wuav.client.gui.utils.AnimationUtil;
import com.wuav.client.gui.utils.enums.CustomColor;
import com.wuav.client.gui.utils.event.CustomEvent;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController extends RootController implements Initializable {
    @FXML
    private TextField queryField;
    @FXML
    private MFXButton createNewUser;
    @FXML
    private TableView<AppUser> userTable;
    @FXML
    private AnchorPane userAnchorPane;
    @FXML
    private Label errorLabel;
    @FXML
    private Pane notificationPane;
    private final EventBus eventBus;
    @FXML
    private TableColumn<AppUser,String> colName,colEmail,colRole,colDate;

    @FXML
    private TableColumn<AppUser,String> colEdit;

    private final IUserModel userModel;

    private final IControllerFactory controllerFactory;

    private boolean isSettings = false;


    @Inject
    public UsersController(IUserModel userModel, IControllerFactory controllerFactory, EventBus eventBus) {
        this.userModel = userModel;
        this.controllerFactory = controllerFactory;
        this.eventBus = eventBus;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTable();
        setupSearchField();
        eventBus.register(this);
        createNewUser.setOnAction(e -> openCreateUserWindow("Create new user",ViewType.USER_MODAL, null));
    }

    private void setupSearchField() {
        queryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                List<AppUser> searchResults = userModel.searchUsers(newValue);
                ObservableList<AppUser> users = FXCollections.observableList(searchResults);
                userTable.setItems(users);
            } else {
                fillTable();
            }
        });
    }

    private void openCreateUserWindow(String title, ViewType viewType, AppUser value) {
        Scene scene = userAnchorPane.getScene();
        Window window = scene.getWindow();
        if (window instanceof Stage) {
            Pane layoutPane = (Pane) scene.lookup("#layoutPane");
            if (layoutPane != null) {
                layoutPane.setStyle(CustomColor.DIMMED.getStyle());
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
                layoutPane.setStyle(CustomColor.TRANSPARENT.getStyle());
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

    /**
     * handle global notification event
     */
    @Subscribe
    public void handleNotificationEvent(CustomEvent event) {
        if (event.getEventType() == EventType.SHOW_NOTIFICATION) {
            errorLabel.setText(event.getMessage());
            boolean isSuccess = (boolean) event.getData();
            if(!isSuccess) AnimationUtil.animateInOut(notificationPane,4, CustomColor.ERROR);
            if(isSuccess) AnimationUtil.animateInOut(notificationPane,4, CustomColor.INFO);
        }
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
        setupColumns();
        setupMenuItem();
        setTableWithUsers();

    }

    private void setupColumns() {
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

    }

    private void setupMenuItem() {

        ImageView editImage = new ImageView("/edit.png");
        ImageView deleteImage = new ImageView("/delete.png");

        editImage.setFitWidth(20);
        editImage.setFitHeight(20);

        deleteImage.setFitWidth(20);
        deleteImage.setFitHeight(20);

        Callback<TableColumn<AppUser, String>, TableCell<AppUser, String>> cellFactory
                = //
                new Callback<TableColumn<AppUser, String>, TableCell<AppUser, String>>() {
                    @Override
                    public TableCell call(final TableColumn<AppUser, String> param) {
                        final TableCell<AppUser, String> cell = new TableCell<AppUser, String>() {

                            final Button btn = new Button("•••");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    MenuItem editItem = new Menu("Edit", editImage); // images with that icon
                                    MenuItem deleteItem = new MenuItem("Delete", deleteImage);

                                    // adding all items to context menu
                                    ContextMenu menu = new ContextMenu(editItem, deleteItem);

                                    menu.getStyleClass().add("menuTable");
                                    deleteItem.setStyle("-fx-text-fill: black;");
                                    editItem.setStyle("-fx-text-fill: black;");


                                    editItem.setOnAction(event -> {
                                        isSettings = true;
                                        openCreateUserWindow("User settings",ViewType.USER_SETTINGS,getTableRow().getItem());
                                        event.consume();
                                    });
                                    deleteItem.setOnAction(event -> {
                                        deleteUsers(getTableRow().getItem());
                                        event.consume();
                                    });

                                    ContextMenu finalMenu = menu;
                                    btn.setOnAction(event -> {
                                        finalMenu.show(btn, Side.BOTTOM, -95, 0);
                                    });
                                    btn.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-cursor: HAND;");
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        colEdit.prefWidthProperty().set(40);
        colEdit.setResizable(false);
        colEdit.setCellFactory(cellFactory);
    }

    private void deleteUsers(AppUser value) {
        var response = AlertHelper.showOptionalAlertWindow("Action warning !","Are you sure you want to delete this user ? ", Alert.AlertType.CONFIRMATION);
            if(response.isPresent() && response.get() == ButtonType.OK){
                boolean userDeleted = userModel.deleteUser(value);
                if(userDeleted) {
                    errorLabel.setText("User with id: " + value.getId() + " deleted successfully");
                    AnimationUtil.animateInOut(notificationPane,4, CustomColor.INFO);
                    refreshTable();
                }else {
                    errorLabel.setText("User with id: " + value.getId() + " could not be delete");
                    AnimationUtil.animateInOut(notificationPane,4, CustomColor.ERROR);
                }
            }
    }

    private void refreshTable() {
        userTable.getItems().clear();
        userTable.setItems(userModel.getAllUsers());
    }

    /**
     * Registering events
     */
    @Subscribe
    public void handleRefresh(RefreshEvent event) {
        if (event.eventType() == EventType.UPDATE_USER_TABLE) {
            refreshTable();
            Scene scene = userAnchorPane.getScene();
            if(scene != null){
                Window window = scene.getWindow();
                if (window instanceof Stage) {
                    Pane layoutPane = (Pane) scene.lookup("#layoutPane");
                    if (layoutPane != null) {
                        layoutPane.setStyle(CustomColor.TRANSPARENT.getStyle());
                        layoutPane.setDisable(false);
                        layoutPane.setVisible(false);
                    } else {
                        System.out.println("Could not refresh the table");
                    }
                }
            }
        }
    }

    private void setTableWithUsers() {
        userTable.setItems(userModel.getAllUsers());
    }
}
