package com.event_bar_easv.gui.models.event;

import com.event_bar_easv.be.Event;
import com.event_bar_easv.be.SpecialTicketType;
import com.event_bar_easv.bll.services.interfaces.IEventService;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class EventModel implements IEventModel {
    private final IEventService eventService;

    private ObservableList<Event> allEventsObservableList;

    private ObservableList<SpecialTicketType> specialTicketTypeObservableList;
    @Inject
    public EventModel(IEventService eventService) {
        this.eventService = eventService;
        this.allEventsObservableList = getAllEvents();
        System.out.println("EventModel: " + allEventsObservableList);
    }

    @Override
    public ObservableList<Event> getAllEvents() {
        allEventsObservableList = FXCollections.observableArrayList(eventService.getAllEvents());
        return allEventsObservableList;
    }

    @Override
    public ObservableList<SpecialTicketType>  getAllSpecialTickets() {
        specialTicketTypeObservableList = FXCollections.observableArrayList(eventService.getAllSpecialTickets());
        return specialTicketTypeObservableList;
    }



    @Override
    public Event getEventById(int id) {
        return allEventsObservableList.stream()
                .filter(event -> event.getEventId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Event getEventByName(String eventName) {
        return allEventsObservableList.stream()
                .filter(event -> event.getEventName().equals(eventName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public int createSpecialTicket(SpecialTicketType specialTicketType) {
        return eventService.createSpecialTicket(specialTicketType);
    }

    @Override
    public int addSpecialTicketToAllEvent(SpecialTicketType specialTicketType){
            List<Integer> collectedIds = allEventsObservableList.stream()
                    .map(Event::getEventId)
                    .toList();

            return eventService.addSpecialTicketToAllEvent(specialTicketType, collectedIds);
    }

    @Override
    public int addSpecialTicketToEvent(SpecialTicketType specialTicketType, int eventId) {
        return eventService.addSpecialTicketToEvent(specialTicketType, eventId);
    }

    @Override
    public int createEvent(Event event) {
        return eventService.createEvent(event);
    }


}
