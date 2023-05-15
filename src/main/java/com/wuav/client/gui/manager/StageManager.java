package com.wuav.client.gui.manager;

import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.ControllerFactory;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StageManager {
    private final Map<String, Stage> stages;

    public StageManager() {
        stages = new HashMap<>();
    }

    public void showStage(String title, Parent parent) {
        Stage stage = stages.get(title);
        if (stage == null) {
            stage = createStage(title);
            stages.put(title, stage);
        }

        stage.setScene(new Scene(parent));
        stage.show();
    }

    private Stage createStage(String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        return stage;
    }

    public RootController loadNodesView(ViewType viewType, IControllerFactory controllerFactory) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }
}