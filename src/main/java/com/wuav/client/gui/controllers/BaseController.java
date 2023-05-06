package com.wuav.client.gui.controllers;


import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.models.user.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class BaseController extends RootController implements Initializable {

    @FXML
    private Label menuItemLabel;
    @FXML
    private VBox userDetailsBox;
    @FXML
    private ImageView userImage;
    @FXML
    private Label userNameField;
    @FXML
    private Label userEmailField;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private ImageView workIcon;
    @FXML
    private MFXButton projectButton;
    @FXML
    private MFXButton createNewProject;
    @FXML
    private VBox sideNavBox;
    @FXML
    private ImageView menuIcon;
    @FXML
    private StackPane app_content;

    @FXML
    private MFXButton expand;

    @FXML
    private AnchorPane slider;

    private final IControllerFactory controllerFactory;

    private final EventBus eventBus;

    private boolean isSidebarExpanded = false;

    private Image defaultImage = new Image("/no_data.png");

    @Inject
    public BaseController(IControllerFactory controllerFactory, EventBus eventBus) {
        this.controllerFactory = controllerFactory;
        this.eventBus = eventBus;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        expand.setStyle("-fx-text-fill: transparent;");

        projectButton.setStyle("-fx-background-color: rgba(234, 234, 234, 0.8);");
        if(!CurrentUser.getInstance().getLoggedUser().getRoles().get(0).getName().equals("TECHNICIAN")){
            projectButton.setText("Projects");
            userImage.setImage(defaultImage);
            System.out.println(CurrentUser.getInstance().getLoggedUser().getRoles().get(0));
        }

        handleExpandControl();
        runInParallel(ViewType.PROJECTS);
    }


    private void hideSidebar() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(slider);
        menuItemLabel.setVisible(false);

        userDetailsBox.setVisible(false);
        slide.setToX(0);
        slide.play();

        Image image = new Image(getClass().getClassLoader().getResource("openExpand.png").toExternalForm());
        app_content.setStyle("-fx-background-color: none;");
      //  sideNavBox.setPadding(new Insets(0, 20, 0, 30));

        menuIcon.setImage(image);

        slider.setPrefWidth(80);
        userNameField.setText("");
        userEmailField.setText("");
        sideNavBox.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setStyle("-fx-text-fill: transparent;");
            }
        });

        slide.setOnFinished(ActionEvent -> {

        });
    }

    private void showSidebar() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(slider);
        userDetailsBox.setVisible(true);
        menuItemLabel.setVisible(true);
        //  slide.setToX(slider.getPrefWidth());

        Image image = new Image(getClass().getClassLoader().getResource("closeExpand.png").toExternalForm());
        menuIcon.setImage(image);
        slide.play();
        // app_content.setStyle("-fx-background-color: black;-fx-opacity: 0.1;");


        slider.setPrefWidth(210); // Replace with your original sidebar width
        userNameField.setText(CurrentUser.getInstance().getLoggedUser().getName()); // Replace with your original text
        userEmailField.setText(CurrentUser.getInstance().getLoggedUser().getEmail()); // Replace with your original text
        sideNavBox.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setStyle("-fx-text-fill: black;");
            }
        });

        slide.setOnFinished(ActionEvent -> {

        });
    }

    private void handleExpandControl() {
        expand.setOnAction(event -> {
            if (isSidebarExpanded) {
                isSidebarExpanded = false;
                hideSidebar();
            } else {
                isSidebarExpanded = true;
                showSidebar();
            }
        });
    }

    @FXML
    private void handleDashBoardPageSwitch() {
        projectButton.setStyle("-fx-background-color: rgba(234, 234, 234, 0.8);");
        eventBus.post(new RefreshEvent(EventType.UPDATE_TABLE));
        runInParallel(ViewType.PROJECTS);
    }
    //endregion

    private void runInParallel(ViewType type) {
        final RootController[] parent = {null};
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() throws IOException {
                parent[0] = loadNodesView(type);
                return null;
            }
        };
        loadDataTask.setOnSucceeded(event -> {
            switchToView(parent[0].getView());
        });
        new Thread(loadDataTask).start();
    }

    /**
     * private method for showing new stages whenever its need
     *
     * @param parent root that will be set
     * @param title  title for new stage
     */
    private void show(Parent parent, String title) {
        Stage stage = new Stage();
        Scene scene = new Scene(parent);

        stage.initOwner(getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }

    private void switchToView(Parent parent) {
        app_content.getChildren().clear();
        app_content.getChildren().add(parent);
    }


    // THIS HAS TO BE FIXED MAYBE I NEED SOME SCENE CONTROLLER SINCE THIS IS MESSSSSS
    @FXML
    public void logoutButton(ActionEvent actionEvent) throws IOException {
        CurrentUser.getInstance().logout();

        getStage().close();
        // this is bad
//        IControllerFactory factory = StartUp.getInjector().getInstance(IControllerFactory.class);
//        IRootController controller = factory.loadFxmlFile(ViewType.LOGIN);
//
//
//        Stage stage = new Stage();
//        Scene scene = new Scene(controller.getView());
//        stage.setTitle("Wuav-login");
//        stage.setResizable(false);
//        stage.setScene(scene);
//        stage.show();
    }
    //endregion


}
