package com.event_bar_easv.gui.controllers;

import com.event_bar_easv.be.user.AppRole;
import com.event_bar_easv.be.user.AppUser;
import com.event_bar_easv.gui.controllers.abstractController.RootController;
import com.event_bar_easv.gui.models.user.IUserModel;
import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class UserController extends RootController implements Initializable {

    @FXML
    private TextField custEmail;
    @FXML
    private TextField costName;
    @FXML
    private MFXCheckbox custActive;
    // SYS USER TABLE
    @FXML
    private TableColumn<AppUser,Integer> sysId;
    @FXML
    private TableColumn<AppUser,String>  sysName;
    @FXML
    private TableColumn<AppUser,String>  sysEmail;
    @FXML
    private TableColumn<AppUser,String>  sysHash;
    @FXML
    private TableColumn<AppUser,String>  sysRoles;
    @FXML
    private TableColumn<AppUser,Boolean>  sysActive;
    @FXML
    private TableView<AppUser> sysTable;

    // CUSTOMER USER TABLE

    @FXML
    private TableColumn<AppUser,Integer> customerId;
    @FXML
    private TableColumn<AppUser,String>  customerName;
    @FXML
    private TableColumn<AppUser,String>  customerEmail;
    @FXML
    private TableColumn<AppUser,String>  customerRole;
    @FXML
    private TableColumn<AppUser,String>  customerEvents;
    @FXML
    private TableColumn<AppUser,Boolean>  customerIsActive;
    @FXML
    private TableView<AppUser> customerTable;

    private final IUserModel userModel;

    @Inject
    public UserController(IUserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         fillCustomerTableWithData();
         fillSysTableWithData();
    }

    private void fillSysTableWithData() {
        sysId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        sysName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        sysEmail.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));
        sysHash.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPasswordHash()));

        sysRoles.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoles().stream()
                .map(AppRole::getName)
                .collect(Collectors.joining(","))
        ));

        sysActive.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isActivated()));
        trySetTableWithSysUsers();
    }

    private void fillCustomerTableWithData() {
        customerId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        customerName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        customerEmail.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));

        customerRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoles().stream()
                .map(AppRole::getName)
                .collect(Collectors.joining(","))
        ));

        // TODO: Somehow display which events they are attending later

//        customerRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoles().stream()
//                .map(AppRole::getName)
//                .collect(Collectors.joining(","))
//        ));

        customerIsActive.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isActivated()));
        trySetTableWithSysUsers();
    }

    private void trySetTableWithSysUsers() {

            var allUsers = userModel.getAllUsers();
            var customers = allUsers.stream()
                    .filter(user -> user.getRoles().stream()
                            .map(AppRole::getName)
                            .toList()
                            .contains("user"))
                    .collect(Collectors.toList());

        var sysRoles = Arrays.asList("administrator", "coordinator"); // add other roles here if needed
        var sysUsers = allUsers.stream()
                .filter(user -> sysRoles.stream()
                        .map(Optional::ofNullable)
                        .anyMatch(userRoles -> userRoles.stream().flatMap(role -> user.getRoles().stream())
                                .map(AppRole::getName)
                                .toList()
                                .contains(userRoles.orElse(null))))
                .collect(Collectors.toList());

            customerTable.setItems(FXCollections.observableList(customers));
            sysTable.setItems(FXCollections.observableList(sysUsers));
        }

    @FXML
    private void createCustomer(ActionEvent actionEvent) {


        AppUser appUser = new AppUser();
        Random random = new Random();
        int id = random.nextInt(Integer.MAX_VALUE);

        Random random2 = new Random();
        int roleId = random2.nextInt(Integer.MAX_VALUE);

        appUser.setId(id);
        appUser.setName(costName.getText());
        appUser.setEmail(custEmail.getText());
        appUser.setActivated(custActive.isSelected());

        AppRole appRole = new AppRole(roleId,"user");
        appUser.setRoles(List.of(appRole));

        var result = userModel.createCustomerService(appUser);
        if(result > 0){
            System.out.println("Customer created");
            refreshTable();
        }

    }


    private void refreshTable() {
        if (customerTable != null) {
            if (customerTable.getItems() != null) {
                customerTable.getItems().clear();
                customerTable.getItems().setAll( userModel.getAllUsers());
            }
        }
    }
}
