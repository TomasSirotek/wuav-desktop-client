package com.event_bar_easv.gui.controllers;

import com.event_bar_easv.be.Event;
import com.event_bar_easv.be.SpecialTicketType;
import com.event_bar_easv.be.Ticket;
import com.event_bar_easv.be.TicketType;
import com.event_bar_easv.be.user.AppRole;
import com.event_bar_easv.be.user.AppUser;
import com.event_bar_easv.bll.utilities.AlertHelper;
import com.event_bar_easv.bll.utilities.email.IEmailSender;
import com.event_bar_easv.bll.utilities.pdf.IPdfGenerator;
import com.event_bar_easv.gui.controllers.abstractController.RootController;
import com.event_bar_easv.gui.models.CurrentUser;
import com.event_bar_easv.gui.models.event.IEventModel;
import com.event_bar_easv.gui.models.user.IUserModel;
import com.google.inject.Inject;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DashboardController extends RootController implements Initializable {

    private final IEventModel eventModel;

    private final IUserModel userModel;

    private final IPdfGenerator pdfGenerator;

    private final IEmailSender emailSender;
    @FXML
    private MenuButton specialTickets;

    private String fileName;

    @FXML
    private Button saveTicket;

    @FXML
    private Button sendTicket;
    @FXML
    private Button viewTicket;

    @FXML
    private ProgressIndicator progressLoad;

    @FXML
    private MenuButton ticketType;

    @FXML
    private MenuButton eventsMenuButton;

    @FXML
    private MenuButton customerMenuButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CurrentUser currentUser = CurrentUser.getInstance();
        currentUser.getLoggedUser().getRoles().get(0);
        fillEvents();
        fillCustomers();
    }


    @Inject
    public DashboardController(IEventModel eventModel, IUserModel userModel, IPdfGenerator pdfGenerator, IEmailSender emailSender) {
        this.eventModel = eventModel;
        this.userModel = userModel;
        this.pdfGenerator = pdfGenerator;
        this.emailSender = emailSender;
    }

    private void fillEvents() {
        List<Event> categoryList = eventModel.getAllEvents();
        if (eventsMenuButton.getItems() != null) {
            eventsMenuButton.getItems().clear();
            categoryList.stream()
                    .map(event -> {
                        CheckMenuItem menuItem = new CheckMenuItem();
                        menuItem.setText(event.getEventName());

                        // Set an EventHandler for each menuItem
                        menuItem.setOnAction(eventHandler -> {
                            eventsMenuButton.setText(menuItem.getText());
                            fillTicketTypesForEvent(event.getEventId());
                            fillTicketWithSpecialTickets(event.getEventId());
                        });

                        return menuItem;
                    })
                    .forEach(menuItem -> eventsMenuButton.getItems().add(menuItem));
        }
    }


    private void fillCustomers() {
        var allUsers = userModel.getAllUsers();

        var sortedUsers = allUsers.stream()
                .filter(user -> user.getRoles().stream()
                        .map(AppRole::getName)
                        .toList()
                        .contains("user"))
                .toList();

        if (customerMenuButton.getItems() != null) {
            customerMenuButton.getItems().clear();
            sortedUsers.stream()
                    .map(customer -> {
                        CheckMenuItem menuItem = new CheckMenuItem();
                        menuItem.setText(customer.getEmail());
                        // Set an EventHandler for each menuItem
                        menuItem.setOnAction(eventHandler -> {
                            customerMenuButton.setText(menuItem.getText());
                        });

                        return menuItem;
                    })
                    .forEach(menuItem -> customerMenuButton.getItems().add(menuItem));
        }
    }



    private void fillTicketTypesForEvent(int id) {
        // needs to fill here with all ticket types set by admin
        Event categoryList = eventModel.getEventById(id);
        if (ticketType.getItems() != null) {
            ticketType.getItems().clear();
            categoryList.getTicketTypes().stream()
                    .map(event -> {
                        CheckMenuItem menuItem = new CheckMenuItem();
                        menuItem.setText(event.getType());

                        menuItem.setOnAction(eventHandler -> {
                            ticketType.setText(menuItem.getText());
                        });

                        return menuItem;
                    })
                    .forEach(menuItem -> ticketType.getItems().add(menuItem));
        }
    }

    private void fillTicketWithSpecialTickets(int id) {
        // needs to fill here with all ticket types set by admin
        Event categoryList = eventModel.getEventById(id);
        if (specialTickets.getItems() != null) {
            specialTickets.getItems().clear();
            categoryList.getSpecialTicketTypes().stream()
                    .map(event -> {
                        CheckMenuItem menuItem = new CheckMenuItem();
                        menuItem.setText(event.getType());

                        menuItem.setOnAction(eventHandler -> {
                            specialTickets.setText(menuItem.getText());
                        });

                        return menuItem;
                    })
                    .forEach(menuItem -> specialTickets.getItems().add(menuItem));
        }
    }

    @FXML
    private void exportTicket(ActionEvent actionEvent) {

       //  System.out.println(getSpecialTicketType(1));


        if (validateInputs()) {

            // customer TO BE ADDED
            var customerEmail = customerMenuButton.getText();
            AppUser customer = userModel.getUserByEmail(customerEmail);

            // event TO BE ADDED
            var eventName = eventsMenuButton.getText();
            Event event2 = eventModel.getEventByName(eventName);

            // type TO BE ADDED TO TICKET
            var ticketTypeName = ticketType.getText();
            TicketType ticketType = event2.getTicketTypes().stream()
                    .filter(ticket -> ticket.getType().equals(ticketTypeName))
                    .findFirst().orElse(null);

            // special optional ticket that MIGHT BE ADDED and exported
            SpecialTicketType specialTicketType = getSpecialTicketType(event2.getEventId());


            System.out.println("EVENT CUSTOMER " + customer);
            System.out.println("EVENT CUSTOMER WILL ATTEND " + event2);
            System.out.println("TICKET TYPE " + ticketType.getType());
          //  System.out.println("SPECIAL TICKET TO ENTER ADDED TO EVENT" + specialTicketType);


            System.out.println("Exporting ticket");
            progressLoad.setVisible(true);

            Ticket ticket = new Ticket();
            TicketType ticketType1 = new TicketType();

            if(specialTicketType != null){

                ticketType1.setType(specialTicketType.getType());
            }{
                ticketType1.setType("");
            }


            // 123456789012
            Random random = new Random();
            long id = (long) (random.nextDouble() * 9000000000000L) + 1000000000000L;

            ticket.setId(1234);
            ticket.setEvent(event2);
            ticket.setTicketType(ticketType);
            ticket.setNumber(String.valueOf(id));
            ticket.setOwner(customer);
            ticket.setValid(true);


            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    // Simulate a delay of 3 seconds
                    Thread.sleep(3000);
                    return pdfGenerator.generatePdf(customer,event2,ticket,specialTicketType);
                }
            };

            task.setOnSucceeded(event -> {
                this.fileName = task.getValue();
                System.out.println(task.getValue());
                viewTicket.setDisable(false);
                saveTicket.setDisable(false);
                sendTicket.setDisable(false);
                progressLoad.setVisible(false);
            });

            task.progressProperty().addListener((observable, oldValue, newValue) -> {
                progressLoad.setProgress(newValue.doubleValue());
            });

            progressLoad.progressProperty().bind(task.progressProperty());
            new Thread(task).start();
        }
    }

    private boolean innerValidateInputs() {
       //  ticketType,eventsMenuButton,customerMenuButton

        Optional<CheckMenuItem> selectedType = ticketType.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem)
                .map(CheckMenuItem.class::cast)
                .filter(CheckMenuItem::isSelected)
                .findFirst();

        Optional<CheckMenuItem> selectedItem = eventsMenuButton.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem)
                .map(CheckMenuItem.class::cast)
                .filter(CheckMenuItem::isSelected)
                .findFirst();

        Optional<CheckMenuItem> selectedCustomer = customerMenuButton.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem)
                .map(CheckMenuItem.class::cast)
                .filter(CheckMenuItem::isSelected)
                .findFirst();

        if(selectedType.isEmpty() || selectedItem.isEmpty() || selectedCustomer.isEmpty()){
            return false;
        }

        return true;
    }
    private boolean validateInputs() {
        boolean isValidated = false;
        if (!innerValidateInputs()) {
            AlertHelper.showDefaultAlert("Please fill all the field! You get the drill", Alert.AlertType.ERROR);
        } else {
            isValidated = true;
        }
        return isValidated;
    }

    @FXML
    private void sendTicketViaEmail(ActionEvent actionEvent) {
            // this does not for cuz the fucking settings on that shirtty  vpn we have to use....
       // Session session =  EmailConnectionFactory.getSession();
       // emailSender.sendEmail(session, "keanu.jacobs86@ethereal.email","TLSEmail Testing Subject", "TLSEmail Testing Body");
    }

    @FXML
    private void openViewTicket(ActionEvent actionEvent) {
        try {
            // Get the URL to the PDF file
            URL pdfFileUrl = getClass().getResource(fileName);

            // Convert the URL to a URI
            URI pdfFileUri = pdfFileUrl.toURI();

            // Create a File object from the URI
            File pdfFile = new File(pdfFileUri);

            // Open the PDF file using the default desktop application

            Desktop.getDesktop().open(new File(pdfFile.toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }


    private SpecialTicketType getSpecialTicketType(int id) {

        Optional<CheckMenuItem> selectedType = specialTickets.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem)
                .map(CheckMenuItem.class::cast)
                .filter(CheckMenuItem::isSelected)
                .findFirst();

        if (selectedType.isPresent()) {
            var nameOfSpecialTicket = selectedType.get().getText();

            Event event = eventModel.getEventById(id);
            AtomicReference<SpecialTicketType> specialTicketType = new AtomicReference<>();
            event.getSpecialTicketTypes().stream()
                    .filter(item -> item instanceof SpecialTicketType)
                    .map(SpecialTicketType.class::cast)
                    .filter(item -> item.getType().equals(nameOfSpecialTicket))
                    .findFirst()
                    .ifPresent(item -> specialTicketType.set(item));

            return specialTicketType.get();
        } else {
            return null;
        }
    }
}
