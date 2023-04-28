package com.wuav.client.gui.controllers.controllerFactory;

import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.gui.controllers.abstractController.RootController;

import java.io.IOException;

public interface IControllerFactory {
    /**
     * Method for loading FXML file from given Enum ViewType
     *
     * @param fxmlFile enum predefined in ViewType
     * @return if successful abstract root controller where .getView() can be accessed
     * @throws IOException if it is not possible to load URL in FXML loader
     */
    RootController loadFxmlFile(ViewType fxmlFile) throws IOException;
}
