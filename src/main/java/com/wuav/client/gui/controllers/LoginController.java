package com.wuav.client.gui.controllers;

import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;


import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends RootController implements Initializable {


    @FXML
    private MFXButton login;
    @FXML
    private StackPane baseContent;
    private final IControllerFactory controllerFactory;

    @Inject
    public LoginController(IControllerFactory controllerFactory) {
        this.controllerFactory = controllerFactory;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }



    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }


    @FXML
    private void login()  {
        System.out.println("login in on");

        var test = tryToLoadView();
        getStage().close();
        show(test.getView(), "test");
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
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    private RootController tryToLoadView() {
        try {
            return loadNodesView(ViewType.MAIN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
