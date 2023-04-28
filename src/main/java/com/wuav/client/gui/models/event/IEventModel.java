package com.wuav.client.gui.models.event;

import com.wuav.client.be.Event;
import com.wuav.client.be.SpecialTicketType;
import javafx.collections.ObservableList;

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
