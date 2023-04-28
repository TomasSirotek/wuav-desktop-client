package com.event_bar_easv.dal.mappers;
import com.event_bar_easv.be.Event;
import com.event_bar_easv.be.SpecialTicketType;
import com.event_bar_easv.be.TicketType;
import com.event_bar_easv.be.user.AppUser;
import org.apache.ibatis.annotations.Param;


import java.sql.Date;
import java.util.List;

public interface EventMapper {

    List<Event> getAllEvents();

    List<SpecialTicketType> getAllSpecialTickets();

    int createSpecialTicketType(SpecialTicketType ticketType);
    int addSpecialTicketToEvent(@Param("eventId") int eventId, @Param("ticketId") int ticketId);

    int createEvent(@Param("id") int id,@Param("title") String title, @Param("loc") String loc,@Param("startDate") Date startDate2, @Param("endDate") Date endDate2, @Param("startTime") String startTime2,@Param("endTime") String endTime2, @Param("notes")String notes2);

    int addTypeToTicket(@Param("eventId") int eventId, @Param("typeId") int typeId);

    int createTypeForTicket(TicketType ticketType);

}