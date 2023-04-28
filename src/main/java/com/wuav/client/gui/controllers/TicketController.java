package com.wuav.client.gui.controllers;

import com.wuav.client.be.Event;
import com.wuav.client.be.SpecialTicketType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.models.event.EventModel;
import com.google.inject.Inject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class TicketController extends RootController implements Initializable {

    @FXML
    private TextField ticketNameField;
    @FXML
    private TextField benfitField;
    @FXML
    private MenuButton eventTicketType;
    @FXML
    private TableView<SpecialTicketType> ticketTable;
    @FXML
    private TableColumn<SpecialTicketType,String> colEventTitle;
    @FXML
    private TableColumn<SpecialTicketType,String> colEventBenefit;
    @FXML

    private TableColumn<SpecialTicketType,String> colEvents;

    private final EventModel eventModel;

    @Inject
    public TicketController(EventModel eventModel) {
        this.eventModel = eventModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTableWithEventsData();
    }


    // GET TICKET TYPES CONNECTED WITH THE EVENTS
    private void fillTableWithEventsData() {

        colEventTitle.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        colEventBenefit.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBenefit()));


        List<Event> allEvents = eventModel.getAllEvents();
        if (eventTicketType.getItems() != null) {
            eventTicketType.getItems().clear();
            allEvents.stream()
                    .map(event -> {
                        CheckMenuItem menuItem = new CheckMenuItem();
                        menuItem.setText(event.getEventName());

                        // Set an EventHandler for each menuItem
                        menuItem.setOnAction(eventHandler -> {
                            eventTicketType.setText(menuItem.getText());
                        });

                        return menuItem;
                    })
                    .forEach(menuItem -> eventTicketType.getItems().add(menuItem));
        }

        trySetEventIntoTable();
    }

    private void trySetEventIntoTable() {
        var test = eventModel.getAllSpecialTickets();
        ticketTable.setItems(test);
    }

    private void refreshTable() {
        if (ticketTable != null) {
            if (ticketTable.getItems() != null) {
                ticketTable.getItems().clear();
                ticketTable.getItems().setAll( eventModel.getAllSpecialTickets());
            }
        }
    }

    @FXML
    private void createSpecialTicket(ActionEvent actionEvent) {

        Optional<CheckMenuItem> selectedType = eventTicketType.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem)
                .map(CheckMenuItem.class::cast)
                .filter(CheckMenuItem::isSelected)
                .findFirst();

        if (ticketNameField.getText().isEmpty() || benfitField.getText().isEmpty()) {
            System.out.println("Required");
        } else if (selectedType.isPresent()) {
            String ticketName = ticketNameField.getText();
            String benefit = benfitField.getText();
            String selectedText = selectedType.get().getText();
            System.out.println("Ticket Name: " + ticketName + ", Benefit: " + benefit + ", Selected Type: " + selectedText);
            addTicketForSpecificEvent(selectedType.get().getText());
        } else if (selectedType.isEmpty() && !ticketNameField.getText().isEmpty() && !benfitField.getText().isEmpty()) {
            String ticketName = ticketNameField.getText();
            String benefit = benfitField.getText();
            System.out.println("Ticket Name: " + ticketName + ", Benefit: " + benefit);

            addDefaultTicketsForAllEvents();
        } else {
            System.out.println("Fill fields");
        }

    }

    private void addTicketForSpecificEvent(String eventName) {

        SpecialTicketType specialTicketType = new SpecialTicketType();
        Random random = new Random();
        int id = random.nextInt(Integer.MAX_VALUE);
        specialTicketType.setId(id);
        specialTicketType.setType(ticketNameField.getText());
        specialTicketType.setBenefit(benfitField.getText());

        Event event = eventModel.getEventByName(eventName);

        int createdTicket = eventModel.createSpecialTicket(specialTicketType);

        if(createdTicket > 0){
            System.out.println("Assigning for specific event");
            int result = eventModel.addSpecialTicketToEvent(specialTicketType,event.getEventId());
            if(result > 0){
                System.out.println("Ticket created");
                refreshTable();
            }else {
                System.out.println("Failed to create ticket");
            }
        }
    }

    private void addDefaultTicketsForAllEvents() {
            SpecialTicketType specialTicketType = new SpecialTicketType();
            Random random = new Random();
            int id = random.nextInt(Integer.MAX_VALUE);
            specialTicketType.setId(id);
            specialTicketType.setType(ticketNameField.getText());
            specialTicketType.setBenefit(benfitField.getText());

            int createdTicket = eventModel.createSpecialTicket(specialTicketType);

            if(createdTicket > 0){
                System.out.println("Assigning for all events");
                int result = eventModel.addSpecialTicketToAllEvent(specialTicketType);
                if(result > 0){
                    System.out.println("Ticket created");
                    refreshTable();
                }else {
                    System.out.println("Failed to create ticket");
                }
            }
    }
}
