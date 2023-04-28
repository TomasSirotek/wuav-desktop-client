package com.event_bar_easv;

import com.event_bar_easv.bll.helpers.ViewType;
import com.event_bar_easv.config.StartUp;
import com.event_bar_easv.gui.controllers.abstractController.IRootController;
import com.event_bar_easv.gui.controllers.controllerFactory.IControllerFactory;
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
        stage.setTitle("Event Manager 2023 ");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}