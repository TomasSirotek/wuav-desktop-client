package com.wuav.client.dal.interfaces;

import com.wuav.client.be.Event;
import com.wuav.client.be.SpecialTicketType;
import com.wuav.client.be.TicketType;

import java.sql.Date;
import java.util.List;

public interface IEventRepository {
    List<Event> getAllEvents();

    int addSpecialTicketToEvent(SpecialTicketType specialTicketType, int eventId);

    int createSpecialTicket(SpecialTicketType specialTicketType);

    List<SpecialTicketType> getAllSpecialTickets();

    int createEvent(int id, String title, String loc, Date startDate2, Date endDate2, String startTime2, String endTime2, String notes2);

    int addTypeToEvent(int id, TicketType ticketType);

    int createTicketType(TicketType ticketType);
}
