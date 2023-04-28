package com.wuav.client;

import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.config.StartUp;
import com.wuav.client.gui.controllers.abstractController.IRootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        StartUp.configure();
        IControllerFactory factory = StartUp.getInjector().getInstance(IControllerFactory.class);
        IRootController controller = factory.loadFxmlFile(ViewType.MAIN);

        Scene scene = new Scene(controller.getView());
        stage.setTitle("Wuav-login");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}