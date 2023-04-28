package com.wuav.client.bll.utilities.pdf;

import com.wuav.client.be.Event;
import com.wuav.client.be.SpecialTicketType;
import com.wuav.client.be.Ticket;
import com.wuav.client.be.user.AppUser;

public interface IPdfGenerator {

    String generatePdf(AppUser customer, Event event2, Ticket ticket, SpecialTicketType specialTicketType);
}
