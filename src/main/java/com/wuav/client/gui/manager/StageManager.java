package com.wuav.client.gui.manager;

import com.wuav.client.be.Project;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Stage manager.
 */
public class StageManager {
    private final Map<String, Stage> stages;

    /**
     * Instantiates a new Stage manager.
     */
    public StageManager() {
        stages = new HashMap<>();
    }

    /**
     * Show stage.
     *
     * @param title  the title
     * @param parent the parent
     */
    public void showStage(String title, Parent parent) {
        Stage stage = stages.get(title);
        if (stage == null) {
            stage = createStage(title);
            stages.put(title, stage);
        }

        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * Show stage.
     *
     * @param title         the title
     * @param parent        the parent
     * @param previousScene the previous scene
     */
    public void showStage(Parent parent, String title, Scene previousScene,List<Project> projectList) {
        Stage stage = new Stage();
        Scene scene = new Scene(parent);

        stage.initOwner(previousScene.getWindow());
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

        if(projectList != null){
            stage.getProperties().put("projectsToExport",projectList); // pass optional model here
        }

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Create stage
     * @param title the title
     * @return the stage
     */
    private Stage createStage(String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        return stage;
    }

    /**
     * Load nodes view root controller.
     *
     * @param viewType         the view type
     * @param controllerFactory the controller factory
     * @return the root controller
     * @throws IOException the io exception
     */
    public RootController loadNodesView(ViewType viewType, IControllerFactory controllerFactory) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }
}