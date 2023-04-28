package com.event_bar_easv.bll.utilities.pdf;

import com.event_bar_easv.be.Event;
import com.event_bar_easv.be.SpecialTicketType;
import com.event_bar_easv.be.Ticket;
import com.event_bar_easv.be.TicketType;
import com.event_bar_easv.be.user.AppUser;

public interface IPdfGenerator {

    String generatePdf(AppUser customer, Event event2, Ticket ticket, SpecialTicketType specialTicketType);
}
