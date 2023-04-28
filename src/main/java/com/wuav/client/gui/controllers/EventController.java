package com.wuav.client.gui.controllers;


import com.wuav.client.be.Event;
import com.wuav.client.be.TicketType;
import com.wuav.client.gui.models.event.IEventModel;
import com.google.inject.Inject;
import com.wuav.client.gui.controllers.abstractController.RootController;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Controller for Movies with the view
 */
public class EventController extends RootController implements Initializable {


    @FXML
    private TextField eventName;
    @FXML
    private MFXDatePicker startDate;
    @FXML
    private MFXDatePicker endDate;
    @FXML
    private TextField eventLocation;
    @FXML
    private TextField startTime;
    @FXML
    private TextField endTime;
    @FXML
    private TextField ticketType; // field of custom Ticket name type liek VIP, VVIP, etc
    @FXML
    private TextArea notes;
    @FXML
    private MenuButton listOfTicketTypes;
    @FXML
    private MenuButton eventTicketType;
    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event,String> colEventTitle;
    @FXML
    private TableColumn<Event,String> colEventDates;
    @FXML
    private TableColumn<Event,String> colEventTimes;
    @FXML
    private TableColumn<Event,String> colEventLocation;
    @FXML
    private TableColumn<Event,String> colEventDescription;
    @FXML
    private TableColumn<Event,String> colEventFreeTicket;

    private List<String> storedTicketTypes = new ArrayList<>();


    private final IEventModel eventModel;

    @Inject
    public EventController(IEventModel eventModel) {
        this.eventModel = eventModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillTableWithEventsData();
    }

    private void fillTableWithEventsData() {

        colEventTitle.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEventName()));

        colEventDates.setCellValueFactory(cellData -> {
            Date startDate = cellData.getValue().getStartDate();
            Date endDate = cellData.getValue().getEndDate();

            return new SimpleStringProperty(startDate.toString() + endDate.toString());
        });

        colEventTimes.setCellValueFactory(cellData -> {
            String startTime = cellData.getValue().getStartTime();
            String endTime = cellData.getValue().getEndTime();

            return new SimpleStringProperty(startTime + endTime);
        });

        colEventLocation.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLocation()));
        colEventDescription.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescription()));

        trySetEventIntoTable();
    }

    private void trySetEventIntoTable() {
        var test = eventModel.getAllEvents();
        System.out.println(test);
        eventTable.setItems(test);
    }


    @FXML
    private void createEvent(ActionEvent actionEvent) {
        // collected values
        var title = eventName.getText();
        var loc = eventLocation.getText();
        var startDate2 =  startDate.getValue();
        var endDate2 = endDate.getValue();
        var startTime2 = startTime.getText();
        var endTime2 = endTime.getText();
        var notes2 = notes.getText();

        List<String> storedTicketTypes2 = storedTicketTypes; // stored types of the event


        Random random = new Random();
        int id = random.nextInt(Integer.MAX_VALUE);


        List<TicketType> constructedTypes = new ArrayList<>();
        // Construct TicketTypes
        for (String ticketType : storedTicketTypes2
             ) {
            Random random1 = new Random();
            int ticketTypeId = random1.nextInt(Integer.MAX_VALUE);
            TicketType ticketType1 = new TicketType();
            ticketType1.setId(ticketTypeId);
            ticketType1.setType(ticketType);
            constructedTypes.add(ticketType1);
        }

        Event event = new Event();
        event.setEventId(id);
        event.setEventName(title);
        event.setStartDate(Date.valueOf(startDate2));
        event.setEndDate(Date.valueOf(endDate2));
        event.setLocation(loc);
        event.setDescription(notes2);

        event.setStartTime(startTime2);
        event.setEndTime(endTime2);

        event.setTicketTypes(constructedTypes);


        var result = eventModel.createEvent(event);
        if(result > 0 ){
            System.out.println("Event created");
            refreshTable();
        } else {
            System.out.println("Event not created");
        }
        // create event here
    }

    @FXML
    private void addTicketTolist(ActionEvent actionEvent) {
        if(!ticketType.getText().equals("")){
            System.out.println("Ticket type added to list");
            storedTicketTypes.add(ticketType.getText());

            throwTypeIntoMenu();
        } else {
            System.out.println("Ticket type is empty");
        }

    }


    private void refreshTable() {
        if (eventTable != null) {
            if (eventTable.getItems() != null) {
                eventTable.getItems().clear();
                eventTable.getItems().setAll( eventModel.getAllEvents());
            }
        }
    }

    private void throwTypeIntoMenu() {
        if (listOfTicketTypes.getItems() != null) {
            listOfTicketTypes.getItems().clear();
            storedTicketTypes.stream()
                    .map(event -> {
                        CheckMenuItem menuItem = new CheckMenuItem();
                        menuItem.setText(event);

                        // Set an EventHandler for each menuItem
                        menuItem.setOnAction(eventHandler -> {
                            listOfTicketTypes.setText(menuItem.getText());

                        });

                        return menuItem;
                    })
                    .forEach(menuItem -> listOfTicketTypes.getItems().add(menuItem));
        }
    }

}
