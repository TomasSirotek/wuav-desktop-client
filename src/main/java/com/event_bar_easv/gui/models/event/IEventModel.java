package com.event_bar_easv.gui.models.event;

import com.event_bar_easv.be.Event;
import com.event_bar_easv.be.SpecialTicketType;
import javafx.collections.ObservableList;

import java.util.List;

public interface IEventModel {
    ObservableList<Event> getAllEvents();

    ObservableList<SpecialTicketType>   getAllSpecialTickets();

    Event getEventById(int id);

    Event getEventByName(String eventName);

    int createSpecialTicket(SpecialTicketType specialTicketType);

    int addSpecialTicketToAllEvent(SpecialTicketType specialTicketType);

    int addSpecialTicketToEvent(SpecialTicketType specialTicketType, int eventId);

    int createEvent(Event event);
}
