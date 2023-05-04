package com.wuav.client.gui.controllers;

import com.google.inject.Inject;
import com.wuav.client.be.*;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.dal.interfaces.IImageRepository;
import com.wuav.client.dal.repository.ImageRepository;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.models.user.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ProjectController extends RootController implements Initializable {


    private final IControllerFactory controllerFactory;
    @FXML
    private AnchorPane projectAnchorPane;
    @FXML
    private MFXButton createNewProject;
    @FXML
    private TableColumn<Project,Button> colEdit;
    @FXML
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<Project, CheckBox> colSelectAll;
    @FXML
    private TableColumn<Project,String> colDate;
    @FXML
    private TableColumn<Project,String> colName;
    @FXML
    private TableColumn<Project,String> colStatus;
    @FXML
    private TableColumn<Project,String> colDes;
    @FXML
    private TableColumn<Project,String> colCustomer;
    @FXML
    private TableColumn<Project,String> colType;

    private final IProjectModel projectModel;


    private Consumer<Project> onProjectSelected;

    @Inject
    public ProjectController(IControllerFactory controllerFactory, IProjectModel projectModel) {
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTable();
       createNewProject.setOnAction(e -> openNewProject());

       // System.out.println(CurrentUser.getInstance().getLoggedUser().toString());
    }

    private void openNewProject() {
        Scene scene = projectAnchorPane.getScene();
        Window window = scene.getWindow();
        if (window instanceof Stage) {
            Pane layoutPane = (Pane) scene.lookup("#layoutPane");
            if (layoutPane != null) {
                layoutPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");
                layoutPane.setDisable(true);
                layoutPane.setVisible(true);

                var test = tryToLoadView();
                show(test.getView(), "Create new project",scene);

            } else {
                System.out.println("AnchorPane not found");
            }
        }
    }

    /**
     * private method for showing new stages whenever its need
     *
     * @param parent root that will be set
     * @param title  title for new stage
     */
    private void show(Parent parent, String title,Scene previousScene) {
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
                layoutPane.setStyle("-fx-background-color: transparent;");
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

    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }



    private RootController tryToLoadView() {
        try {
            return loadNodesView(ViewType.ACTIONS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void setTableWithProjects() {
        // get user projects from current logged user singleton class
        List<Project> updatedProjects = projectModel.getProjectByUserId(CurrentUser.getInstance().getLoggedUser().getId());

        // Update projects list in CurrentUser singleton
        CurrentUser.getInstance().getLoggedUser().setProjects(updatedProjects);

        // Set the updated projects list to the table
        ObservableList<Project> projects = FXCollections.observableList(updatedProjects);
        projectTable.setItems(projects);
    }

    private void fillTable() {
        colSelectAll.setCellValueFactory(col -> {

            CheckBox checkBox = new CheckBox();
            checkBox.getStyleClass().add("checked-box");
            // set to be in the center of the cell
            checkBox.setAlignment(Pos.CENTER);
            checkBox.setOnAction(e -> {
              //  System.out.println("Selected: " + col.getValue());
            });
            return new SimpleObjectProperty<>(checkBox);
        });

        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

//        // status
//        colStatus.setCellValueFactory(cellData -> {
//            // create button with image and text
//            MFXButton successInActiveButton = new MFXButton("Success");
//            successInActiveButton.getStyleClass().add("success");
//            // set button to be smaller with padding all around
//            //  successInActiveButton.setMinSize(100, 20);
//
//            successInActiveButton.setRippleAnimateBackground(false);
//            successInActiveButton.setRippleBackgroundOpacity(0);
//            successInActiveButton.setRippleRadius(0);
//            successInActiveButton.setPrefWidth(100);
//            successInActiveButton.setPrefHeight(20);
//
//            // successInActiveButton.setPadding(new Insets(5, 5, 5, 5));
//            successInActiveButton.setOnAction(e -> {
//                System.out.println("Selected: " + cellData.getValue());
//            });
//            // set image to a button
//            var imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/openExpand.png")));
//            imageIcon.setFitHeight(15);
//            imageIcon.setFitWidth(15);
//            successInActiveButton.setGraphic(imageIcon);
//
//            return new SimpleObjectProperty<>(successInActiveButton);
//        });

//        colStatus.setCellValueFactory(cellData -> {
//            String status = cellData.getValue().getStatus();
//            return new SimpleStringProperty(status);
//        });

        colStatus.setCellFactory(column -> {
            return new TableCell<Project, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                        getStyleClass().removeAll("active", "completed");
                    } else {
                        setText(item);
                        if (item.equals("ACTIVE")) {
                            getStyleClass().add("active");
                        } else if (item.equals("COMPLETED")) {
                            getStyleClass().add("success");
                        }
                    }
                }
            };
        });

        // description
        colDes.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        // Customer
    //    colCustomer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getEmail()));

        // Type
      //  colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustomerType().getName()));


        // Date
        colDate.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getCreatedAt();
            return new SimpleStringProperty(date == null ? "No data" : date.toString());
        });

        colEdit.setCellValueFactory(col -> {
            MFXButton playButton = new MFXButton("");
            //  playButton.getStyleClass().add("success");
            playButton.setPrefWidth(100);
            playButton.setPrefHeight(20);
            var imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/edit.png")));
            imageIcon.setFitHeight(15);
            imageIcon.setFitWidth(15);
            playButton.setGraphic(imageIcon);
            playButton.setOnAction(e -> {
              //  System.out.println("Selected: " + col.getValue());
               //  projectModel.setCurrentProject(col.getValue());
             //   setProjectToView(col.getValue());


                runInParallel(ViewType.PROJECT_ACTIONS,col.getValue());

            });
            return new SimpleObjectProperty<>(playButton);
        });

        setTableWithProjects();

    }


    private void setProjectToView(Project project) {
      //  eventBus.post(new ProjectEvent(EventType.SET_CURRENT_PROJECT, project));
    }

    private void runInParallel(ViewType type,Project project) {
        final RootController[] parent = {null};
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() throws IOException {
                parent[0] = loadNodesView(type);
                return null;
            }
        };
        loadDataTask.setOnSucceeded(event -> {
            ProjectActionController controller = (ProjectActionController) parent[0];
            controller.setCurrentProject(project);
            System.out.println("Loaded controller: " + parent[0].getClass().getName());

            switchToView(parent[0].getView());
        });
        new Thread(loadDataTask).start();
    }

    private void switchToView(Parent parent) {
        Scene scene = projectAnchorPane.getScene();
        Window window = scene.getWindow();
        if (window instanceof Stage) {
            StackPane layoutPane = (StackPane) scene.lookup("#app_content");
            layoutPane.getChildren().clear();
            layoutPane.getChildren().add(parent);
        }

    }



}