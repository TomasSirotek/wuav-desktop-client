package com.event_bar_easv.bll.services.interfaces;

import com.event_bar_easv.be.Event;
import com.event_bar_easv.be.SpecialTicketType;

import java.util.List;

public interface IEventService {

    List<Event> getAllEvents();

    List<SpecialTicketType> getAllSpecialTickets();

    int addSpecialTicketToAllEvent(SpecialTicketType specialTicketType, List<Integer> collectedIds);

    int addSpecialTicketToEvent(SpecialTicketType specialTicketType, int eventId);

    int createSpecialTicket(SpecialTicketType specialTicketType);

    int createEvent(Event event);
}
