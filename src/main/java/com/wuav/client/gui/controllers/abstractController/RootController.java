package com.wuav.client.gui.controllers.abstractController;

import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Abstract class that cannot be instantiated it is used for setting up view when its loaded
 * serves just purpose of setting and getting view when it is needed with little help of abstraction
 */
public abstract class RootController implements IRootController {
    protected Parent root;

    /**
     * method to retrieve Parent root after it is set in controller factory
     *
     * @return Parent root that is needed in order to construct scene
     */
    @Override
    public Parent getView() {
        return root;
    }

    /**
     * method to set view after it is properly loaded from controller factory
     *
     * @param node root that will be stored and after retrieved when needed
     */
    @Override
    public void setView(Parent node) {
        this.root = Objects.requireNonNull(node, "view must not be null.");
    }

    /**
     * method to retrieve current stage from the Parent root also all the children that extends the root controller are able to use it
     *
     * @return stage for currently set Parent view
     */
    @Override
    public Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }

}
