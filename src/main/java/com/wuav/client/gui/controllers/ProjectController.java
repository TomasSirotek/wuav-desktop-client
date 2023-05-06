package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.wuav.client.be.*;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.bll.utilities.email.EmailConnectionFactory;
import com.wuav.client.bll.utilities.email.IEmailSender;
import com.wuav.client.bll.utilities.engines.IEmailEngine;
import com.wuav.client.bll.utilities.pdf.IPdfGenerator;
import com.wuav.client.bll.utilities.pdf.PdfGenerator;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.AlertHelper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.application.Platform;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import javax.mail.Session;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class ProjectController extends RootController implements Initializable {


    private final IControllerFactory controllerFactory;
    @FXML
    private HBox projectCreationStatus;
    @FXML
    private MFXProgressSpinner tableDataLoad;
    @FXML
    private CheckBox selectAllTableCheck;

    @FXML
    private Label projectLabelMain;

    @FXML
    private TableColumn<Project,Button> colDelete;

    @FXML
    private Label emailLoadLabel;
    @FXML
    private Pane emailLoadPane;

    @FXML
    private MFXButton exportSelected;
    @FXML
    private TableColumn<Project,Button> colEmail;
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
    private TableColumn<Project,String> colDes;
    @FXML
    private TableColumn<Project,String> colCustomer;
    @FXML
    private TableColumn<Project,String> colType;

    private final IProjectModel projectModel;

    private final IEmailSender emailSender;

    private final IEmailEngine emailEngine;

    private Consumer<Project> onProjectSelected;

    private List<CheckBox> checkBoxList = new ArrayList<>();

    List<Project> selectedProjects = new ArrayList<>();

    private final EventBus eventBus;


    @Inject
    public ProjectController(IControllerFactory controllerFactory, IProjectModel projectModel, IEmailSender emailSender, IEmailEngine emailEngine, EventBus eventBus) {
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;
        this.emailSender = emailSender;
        this.emailEngine = emailEngine;
        this.eventBus = eventBus;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTable();
       createNewProject.setOnAction(e -> openActionWindows("Create new project",ViewType.ACTIONS,null));
       exportSelected.setOnAction(e -> exportSelected());
        eventBus.register(this);

        selectAllTableCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateCheckBoxes(newValue);
        });
    }

    private void updateCheckBoxes(boolean selectAll) {
        selectedProjects.clear();

        List<Project> items = projectTable.getItems();

        for (int i = 0; i < checkBoxList.size(); i++) {
            CheckBox checkBox = checkBoxList.get(i);
            checkBox.setSelected(selectAll);

            if (i < items.size()) {
                Project project = items.get(i);
                if (selectAll) {
                    selectedProjects.add(project);
                }
            }
        }
    }

    private void exportSelected() {
        if(selectedProjects.isEmpty()){
            AlertHelper.showDefaultAlert("No projects yet to be selected for export ",Alert.AlertType.WARNING);
            return;
        }
        selectedProjects.forEach(p -> System.out.println("now its selected -> " + p.getId()  + " " + p.getName()));
        openActionWindows("Export selected projects",ViewType.EXPORT,selectedProjects);

    }

    private void openActionWindows(String title,ViewType viewType,List<Project> projectList){
        Scene scene = projectAnchorPane.getScene();
        Window window = scene.getWindow();
        if (window instanceof Stage) {
            Pane layoutPane = (Pane) scene.lookup("#layoutPane");
            if (layoutPane != null) {
                layoutPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");
                layoutPane.setDisable(true);
                layoutPane.setVisible(true);

                var test = tryToLoadView(viewType);
                show(test.getView(), title,scene,projectList);

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
    private void show(Parent parent, String title, Scene previousScene,List<Project> projectList) {
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

        if(projectList != null){
            stage.getProperties().put("projectsToExport",projectList); // pass optional model here
        }


        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }



    private RootController tryToLoadView(ViewType viewType) {
        try {
            return loadNodesView(viewType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    // TODO : DEPENDING ON THE ROLE GET PROJECTS BY USER ID
    // ADMIN - GET ALL PROJECTS FROM ALL APP USERS THAT ARE TECHNICIANS
    // TECHNICIAN - GET ALL PROJECTS FOR HIM BY ID
    // SALESMAN - GET ALL PROJECTS FROM ALL TECHNICIANS
    private void setTableWithProjects() {
        // get user projects from current logged user singleton class
      //  emailLoadPane.setVisible(true);
      //  emailLoadPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");
      //  emailLoadLabel.setText("Loading projects");
        tableDataLoad.setVisible(true);




        Task<List<Project>> loadProjectsTask = new Task<>() {
            @Override
            protected List<Project> call() {
                if (CurrentUser.getInstance().getLoggedUser().getRoles().get(0).getName().equals("TECHNICIAN")) {
                    // get user projects from current logged user singleton class
                    return projectModel.getProjectsByUserId(CurrentUser.getInstance().getLoggedUser().getId());
                } else {
                    projectLabelMain.setText("Projects");
                    return projectModel.getAllProjects();
                }
            }
        };

        // Update the UI when the task is completed
        loadProjectsTask.setOnSucceeded(event -> {
            List<Project> updatedProjects = loadProjectsTask.getValue();

            // Update projects list in CurrentUser singleton
            CurrentUser.getInstance().getLoggedUser().setProjects(updatedProjects);

            // Set the updated projects list to the table
            ObservableList<Project> projects = FXCollections.observableList(updatedProjects);

            tableDataLoad.setVisible(false);
            projectTable.setItems(projects);

        //    emailLoadPane.setVisible(false);
        //    emailLoadPane.setStyle("-fx-background-color: transparent");
         //   emailLoadLabel.setText("");
        });

        // Handle any errors during the task execution
        loadProjectsTask.setOnFailed(event -> {
            // Handle error appropriately
            System.out.println(event.getSource().getException());
        });

        // Run the task in a new thread
        new Thread(loadProjectsTask).start();
    }

    /**
     * Registering events
     */
    @Subscribe
    public void handleCategoryEvent(RefreshEvent event) {
        if (event.eventType() == EventType.UPDATE_TABLE) {
            System.out.println("refreshing event");
            projectCreationStatus.setVisible(true);
            // Retrieve the updated projects list from your data source
            List<Project> updatedProjects = projectModel.getProjectsByUserId(CurrentUser.getInstance().getLoggedUser().getId());

            // Update the cache in the ProjectModel
            projectModel.updateProjectsCache(CurrentUser.getInstance().getLoggedUser().getId(), updatedProjects);

            // Refresh the table with the updated projects list
            ObservableList<Project> projects = FXCollections.observableList(updatedProjects);
            projectTable.setItems(projects);
            projectCreationStatus.setVisible(false);

        }
    }


    private void fillTable() {
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
                            if (newSelected) {
                                selectedProjects.add(project);
                            } else {
                                selectedProjects.remove(project);
                            }
                        });
                        checkBoxList.add(checkBox);
                    }
                    setGraphic(checkBox);
                }
            }
        });
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));



        // description
        colDes.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
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
                runInParallel(ViewType.PROJECT_ACTIONS,col.getValue());

            });
            return new SimpleObjectProperty<>(playButton);
        });

        colEmail.setCellValueFactory(project -> {
            MFXButton playButton2 = new MFXButton("");
            //  playButton.getStyleClass().add("success");
            playButton2.setPrefWidth(100);
            playButton2.setPrefHeight(20);
            var imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/emailIcon.png")));
            imageIcon.setFitHeight(15);
            imageIcon.setFitWidth(15);
            playButton2.setGraphic(imageIcon);

            playButton2.setOnAction(e -> {
                // open window with choosing to whom to email it
                sendReportViaEmail(
                        CurrentUser.getInstance().getLoggedUser(),
                        project.getValue()
                );
            });
            return new SimpleObjectProperty<>(playButton2);
        });

        colDelete.setCellValueFactory(project -> {
            MFXButton playButton2 = new MFXButton("");
            //  playButton.getStyleClass().add("success");
            playButton2.setPrefWidth(100);
            playButton2.setPrefHeight(20);
            var imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/delete.png")));
            imageIcon.setFitHeight(15);
            imageIcon.setFitWidth(15);
            playButton2.setGraphic(imageIcon);

            playButton2.setOnAction(e -> {
                System.out.println("delete project" + project.getValue());

            });
            return new SimpleObjectProperty<>(playButton2);
        });

        setTableWithProjects();

    }



    private void sendReportViaEmail(AppUser appUser,Project project) {

        Session session = EmailConnectionFactory.getSession();
        // genereate pdf report and send it via email
        // convert stream to file

        new Thread(() -> {
            try {

                projectCreationStatus.setVisible(true);
                File generatedPdf = null;
                try {
                    generatedPdf = generatePDFToFile(appUser,project,"installation-report" + project.getCustomer().getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Define the template name and variables
                String templateName = "email-template";
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("customerName", project.getCustomer().getName());
                templateVariables.put("technician", appUser.getName());
                templateVariables.put("technicianEmail", appUser.getEmail());
                templateVariables.put("installationDate", project.getCreatedAt());
                templateVariables.put("customerType", project.getCustomer().getType());

                // Process the template and generate the email body
                String emailBody = emailEngine.processTemplate(templateName, templateVariables);
                // Update the UI on the JavaFX application thread
                var emailResult = emailSender.sendEmail(session, "vince.kautzer@ethereal.email","Installation completed", emailBody,true,generatedPdf);
                Platform.runLater(() -> {
                    // Hide the progress bar
                   // progressLoader.setVisible(false);

                    // Display message
                    if (emailResult) {
                        AlertHelper.showDefaultAlert("Email successfully sent ", Alert.AlertType.INFORMATION);
                        projectCreationStatus.setVisible(false);
                    }
                });
            } catch (Exception e) {
                // Handle sending failure
                Platform.runLater(() -> {
                    // Hide the progress bar
                 //   progressLoader.setVisible(false);
                    projectCreationStatus.setVisible(false);
                    // Show an error message
                    AlertHelper.showDefaultAlert("Email sending failed " + e.getMessage(), Alert.AlertType.ERROR);
                });
            }
        }).start();

    }

    private static File generatePDFToFile(AppUser appUser,Project project,String fileName) throws IOException {
        IPdfGenerator pdfGenerator = new PdfGenerator();
        ByteArrayOutputStream stream = pdfGenerator.generatePdf(appUser,project,fileName);

        // Convert stream to byte array
        byte[] pdfBytes = stream.toByteArray();

        // Create a temporary file and write the PDF bytes to it
        File pdfFile = File.createTempFile(fileName, ".pdf");
        OutputStream os = new FileOutputStream(pdfFile);
        os.write(pdfBytes);
        os.close();

        return pdfFile;

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