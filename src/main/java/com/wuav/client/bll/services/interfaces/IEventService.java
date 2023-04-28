package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Event;
import com.wuav.client.be.SpecialTicketType;

import java.util.List;

public interface IEventService {

    List<Event> getAllEvents();

    List<SpecialTicketType> getAllSpecialTickets();

    int addSpecialTicketToAllEvent(SpecialTicketType specialTicketType, List<Integer> collectedIds);

    int addSpecialTicketToEvent(SpecialTicketType specialTicketType, int eventId);

    int createSpecialTicket(SpecialTicketType specialTicketType);

    int createEvent(Event event);
}
