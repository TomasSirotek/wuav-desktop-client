package com.wuav.client.gui.controllers;

import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.models.user.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModalActionController extends RootController implements Initializable {

    @FXML
    private MFXTextField projectNameField;
    @FXML
    private AnchorPane modalPane;
    @FXML
    private MFXButton createNewProject;

    private final IControllerFactory controllerFactory;

    private IProjectModel projectModel;

    @Inject
    public ModalActionController(IControllerFactory controllerFactory, IProjectModel projectModel) {
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       createNewProject.setOnAction(e -> createNewProject());
    }
    private void createNewProject() {
        // reach project service and create new project and pass the project name
        // if project is succesfully

        // get current logged user id
       int userId =  CurrentUser.getInstance().getLoggedUser().getId();
       Project project = projectModel.createProjectByName(userId,projectNameField.getText().trim());
         if(project != null){
             projectModel.setCurrentProject(project);
             runInParallel(ViewType.PROJECT_ACTIONS);
         }
    }

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

    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }

    private void switchToView(Parent parent) {
        Stage  test = (Stage) getStage().getProperties().get("previousStage");
        Scene previousScene = test.getScene();
        Pane layoutPane = (Pane) previousScene.lookup("#layoutPane");
        StackPane appContent = (StackPane) previousScene.getRoot().lookup("#app_content");
        if (appContent != null || layoutPane != null) {
            getStage().close();
            layoutPane.setDisable(true);
            layoutPane.setStyle("-fx-background-color: transparent;");
            appContent.getChildren().clear();
            appContent.getChildren().add(parent);
        }
    }

    private Parent getParent(Scene scene) {
        Parent parent = scene.getRoot();
        while (parent != null && !(parent instanceof AnchorPane)) {
            parent = parent.getParent();
        }
        return parent;
    }

    //endregion


}
