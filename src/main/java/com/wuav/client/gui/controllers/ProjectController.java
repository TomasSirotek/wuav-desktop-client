package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.wuav.client.be.*;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import javafx.stage.Modality;
import org.jsoup.Jsoup;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.manager.StageManager;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.AlertHelper;
import com.wuav.client.gui.utils.AnimationUtil;
import com.wuav.client.gui.utils.enums.CustomColor;
import com.wuav.client.gui.utils.enums.UserRoleType;
import com.wuav.client.gui.utils.event.CustomEvent;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class ProjectController extends RootController implements Initializable {

    @FXML
    private HBox projectCreationStatus,exportToggleHbox,actionToggleHbox;
    @FXML
    private MFXProgressSpinner tableDataLoad;
    @FXML
    private CheckBox selectAllTableCheck;
    @FXML
    private Pane notificationPane;
    @FXML
    private Label projectLabelMain,errorLabel;
    @FXML
    private MFXButton exportSelected,createNewProject;
    @FXML
    private AnchorPane projectAnchorPane;
    @FXML
    private TableColumn<Project,String> colEdit;
    @FXML
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<Project, CheckBox> colSelectAll;
    @FXML
    private TableColumn<Project,String> colDate,colName,colDes,colCustomer,colType;
    private List<CheckBox> checkBoxList = new ArrayList<>();

    private List<Project> selectedProjects = new ArrayList<>();

    private final EventBus eventBus;
    private final IProjectModel projectModel;

    private final IControllerFactory controllerFactory;

    private final StageManager stageManager;

    @Inject
    public ProjectController(IControllerFactory controllerFactory, IProjectModel projectModel, EventBus eventBus, StageManager stageManager) {
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;
        this.eventBus = eventBus;
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventBus.register(this);
        fillTable();
        setupActions();
        swapButtonsInNonTechnicianRole();
    }

    /**
     * This method is used to set up actions for table preview
     */
    private void setupActions() {
        createNewProject.setOnAction(e -> openActionWindows("Create new project",ViewType.ACTIONS,null));
        exportSelected.setOnAction(e -> exportSelected());
        selectAllTableCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateCheckBoxes(newValue);
        });
    }

    /**
     * This method is used swap the buttons when the user is not a technician in order
     * to disallow the user to create projects
     */
    private void swapButtonsInNonTechnicianRole() {
        if(!CurrentUser.getInstance().getLoggedUser().getRoles().get(0).getName().equals(UserRoleType.TECHNICIAN.toString())){
            AtomicReference<MFXButton> storedButton = new AtomicReference<>();

            exportToggleHbox.getChildren().forEach(node -> {
                if(node instanceof MFXButton){
                    MFXButton button = (MFXButton) node;
                    storedButton.set(button);
                }
            });

            // clean children in the action hbox and replace with the stored button
            actionToggleHbox.getChildren().clear();
            actionToggleHbox.getChildren().add(storedButton.get());
        }
    }


    /**
     * This method is to set up and updated the checkboxes
     */
    private void updateCheckBoxes(boolean selectAll) {
        selectedProjects.clear();

        List<Project> items = projectTable.getItems();

        IntStream.range(0, checkBoxList.size())
                .filter(i -> i < items.size())
                .forEach(i -> {
                    CheckBox checkBox = checkBoxList.get(i);
                    checkBox.setSelected(selectAll);

                    Project project = items.get(i);
                    if (selectAll) {
                        selectedProjects.add(project);
                    }
                });
    }

    /**
     * This method is open projects for export
     */
    private void exportSelected() {
        if(selectedProjects.isEmpty()){
            errorLabel.setText("No projects yet to be selected for export");
            AnimationUtil.animateInOut(notificationPane,2, CustomColor.INFO);
            return;
        }
        openActionWindows("Export selected projects",ViewType.EXPORT,selectedProjects);
    }

    private void openActionWindows(String title,ViewType viewType,List<Project> projectList){
        Scene scene = projectAnchorPane.getScene();
        Window window = scene.getWindow();
        if (window instanceof Stage) {
            Pane layoutPane = (Pane) scene.lookup("#layoutPane");
            if (layoutPane != null) {
                layoutPane.setStyle(CustomColor.DIMMED.getStyle());
                layoutPane.setDisable(true);
                layoutPane.setVisible(true);

                try {
                    loadNewView(title,viewType,projectList,scene);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                System.out.println("AnchorPane not found");
            }
        }
    }


    /**
     * Load the new view
     * @throws IOException
     */
    private void loadNewView(String title,ViewType viewType,List<Project> projectList,Scene scene) throws IOException {
        RootController rootController = stageManager.loadNodesView(
                viewType,
                controllerFactory
        );
        stageManager.showStage(rootController.getView(), title,scene,projectList);
    }

    /**
     * This method is used to fill the table with projects
     * STILL HAVE TO FIX CATCHING THE EXCEPTION CORRECTLY
     */
    private void setTableWithProjects() {
        tableDataLoad.setVisible(true);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Callable<List<Project>> loadProjectsTask = () -> {
            AppUser user = CurrentUser.getInstance().getLoggedUser();
            IUserRoleStrategy strategy = CurrentUser.getInstance().getUserRoleStrategy();
            return strategy.getProjects(user);
        };

        Future<List<Project>> future = executorService.submit(loadProjectsTask);

        executorService.shutdown();
        try {
            List<Project> updatedProjects = future.get();
            // Update projects list in CurrentUser singleton
            CurrentUser.getInstance().getLoggedUser().setProjects(updatedProjects);
            // Set the updated projects list to the table
            ObservableList<Project> projects = FXCollections.observableList(updatedProjects);

            tableDataLoad.setVisible(false);
            projectTable.setItems(projects);
        } catch (InterruptedException | ExecutionException e) {
            Throwable cause = e.getCause();
            AnimationUtil.animateInOut(notificationPane,4, CustomColor.ERROR);
            errorLabel.setText(cause != null ? cause.getMessage() : e.getMessage());
        }
    }


    private void refreshTable(){
        List<Project> updatedProjects = projectModel.getProjectsByUserId(CurrentUser.getInstance().getLoggedUser().getId());
        // Update the cache in the ProjectModel
        projectModel.updateProjectsCache(CurrentUser.getInstance().getLoggedUser().getId(), updatedProjects);

        // Refresh the table with the updated projects list
        ObservableList<Project> projects = FXCollections.observableList(updatedProjects);
        projectTable.setItems(projects);
    }

    /**
     * Registering events
     */
    @Subscribe
    public void handleCategoryEvent(RefreshEvent event) {
        if (event.eventType() == EventType.UPDATE_TABLE) {
            projectCreationStatus.setVisible(true);
            // Retrieve the updated projects list from your data source
            refreshTable();
            projectCreationStatus.setVisible(false);

        }
    }


    private void fillTable() {
        setupTableCheckBoxes();
        setupTableColumns();
        setupTableOptions();
        // set projects list to the table
        setTableWithProjects();

    }

    private void setupTableOptions() {
        ImageView editImage = new ImageView("/edit.png");

        ImageView emailImage = new ImageView("/emailIcon.png");
        ImageView deleteImage = new ImageView("/delete.png");

        editImage.setFitWidth(20);
        editImage.setFitHeight(20);
        emailImage.setFitWidth(20);
        emailImage.setFitHeight(20);
        deleteImage.setFitWidth(20);
        deleteImage.setFitHeight(20);

        Callback<TableColumn<Project, String>, TableCell<Project, String>> cellFactory
                = //
                new Callback<TableColumn<Project, String>, TableCell<Project, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Project, String> param) {
                        final TableCell<Project, String> cell = new TableCell<Project, String>() {

                            final Button btn = new Button("•••");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    MenuItem editItem = new Menu("Edit", editImage); // images with that icon
                                    MenuItem emailItem = new MenuItem("Send email", emailImage);
                                    MenuItem deleteItem = new MenuItem("Delete", deleteImage);

                                    // adding all items to context menu
                                    ContextMenu menu = null;

                                    if(CurrentUser.getInstance().getLoggedUser().getRoles().get(0).getName().equals(UserRoleType.ADMIN.name())){
                                        menu = new ContextMenu(editItem, emailItem, deleteItem);
                                    }else {
                                        menu = new ContextMenu(editItem, emailItem);
                                    }
                                    menu.getStyleClass().add("menuTable");
                                    deleteItem.setStyle("-fx-text-fill: black;");
                                    editItem.setStyle("-fx-text-fill: black;");
                                    emailItem.setStyle("-fx-text-fill: black;");

                                    editItem.setOnAction(event -> {
                                        // edit here
                                        runInParallel(ViewType.PROJECT_ACTIONS,getTableRow().getItem());
                                        event.consume();
                                    });
                                    emailItem.setOnAction(event -> {
                                        // email here
                                        openPdfBuilder(getTableRow().getItem());
                                        event.consume();
                                    });
                                    deleteItem.setOnAction(event -> {
                                        deleteProject(getTableRow().getItem());
                                        event.consume();
                                    });

                                    ContextMenu finalMenu = menu;
                                    btn.setOnAction(event -> {
                                        finalMenu.show(btn, Side.BOTTOM, -95, 0);
                                    });
                                    btn.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-cursor: HAND;");
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        colEdit.prefWidthProperty().set(40);
        colEdit.setResizable(false);
        colEdit.setCellFactory(cellFactory);
    }

    private void setupTableColumns() {
        // Name
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        // Description needs to be cleared of the html tags form the db
        colDes.setCellValueFactory(cellData -> {
            String htmlDescription = cellData.getValue().getDescription();
            String plainDescription = Jsoup.parse(htmlDescription).text();
            return new SimpleStringProperty(plainDescription);
        });

        // Customer
        colCustomer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getEmail()));
        // Type
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getType()));
        // Date
        colDate.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getCreatedAt();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMMM dd yyyy");
            String formattedDate = dateFormat.format(date);

            return new SimpleStringProperty(date == null ? "No data" : formattedDate);
        });

    }

    private void setupTableCheckBoxes() {
        colSelectAll.setCellFactory(param -> new TableCell<Project, CheckBox>() {
            private CheckBox checkBox;

            @Override
            protected void updateItem(CheckBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    if (checkBox == null) {
                        checkBox = new CheckBox();
                        checkBox.getStyleClass().add("checked-box");
                        checkBox.setAlignment(Pos.CENTER);
                        checkBox.selectedProperty().addListener((obs, oldSelected, newSelected) -> {
                            Project project = getTableView().getItems().get(getIndex());
                            if(newSelected)  selectedProjects.add(project);
                            if(!newSelected)  selectedProjects.remove(project);
                        });
                        checkBox.setStyle("-fx-cursor: HAND;");
                        checkBoxList.add(checkBox);
                    }
                    setGraphic(checkBox);

                }
            }
        });
    }


    /**
     * open pdf builder method
     */
    private void openPdfBuilder(Project project) {
        RootController controller = tryToLoadView(ViewType.PDF_BUILDER);
        eventBus.post(new RefreshEvent(EventType.EXPORT_EMAIL));

        Stage stage = new Stage();
        Scene scene = new Scene(controller.getView());

        stage.initOwner(getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Build you PDF");
        stage.setOnCloseRequest(e -> {

        });
        // set on showing event to know about the previous stage so that it can be accessed from modalAciton controlelr
        stage.setOnShowing(e -> {
            stage.getProperties().put("projectToExport", project);
        });
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    private RootController tryToLoadView(ViewType viewType) {
        try {
            return loadNodesView(viewType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * handle global notification event
     */
    @Subscribe
    public void handleNotificationEvent(CustomEvent event) {
        if (event.getEventType() == EventType.SHOW_NOTIFICATION) {
            errorLabel.setText(event.getMessage());
            boolean isSuccess = (boolean) event.getData();
            if(!isSuccess) AnimationUtil.animateInOut(notificationPane,4, CustomColor.ERROR);
            if(isSuccess) AnimationUtil.animateInOut(notificationPane,4, CustomColor.INFO);
        }
    }

    /**
     * Delete project
     * @param project project to be deleted
     */
    private void  deleteProject(Project project){
        var response = AlertHelper.showOptionalAlertWindow("Action warning !","Are you sure you want to delete this project ? ", Alert.AlertType.CONFIRMATION);
        if(response.isPresent() && response.get() == ButtonType.OK){
            boolean projectDeleted = projectModel.deleteProject(project);

            errorLabel.setText("Project with id: " + project.getId() + " has been deleted");
            if(!projectDeleted) AnimationUtil.animateInOut(notificationPane,4, CustomColor.ERROR);
            AnimationUtil.animateInOut(notificationPane,4, CustomColor.INFO);
            refreshTable();
        }
    }


    /**
     * Load nodes view in parallel
     * @param type view type
     * @return root controller
     * @throws IOException
     */
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
    private RootController loadNodesView(ViewType viewType) throws IOException {
       return controllerFactory.loadFxmlFile(viewType);
   }




}