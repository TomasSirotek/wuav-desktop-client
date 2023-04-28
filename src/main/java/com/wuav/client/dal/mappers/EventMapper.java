package com.wuav.client.dal.mappers;
import com.wuav.client.be.Event;
import com.wuav.client.be.SpecialTicketType;
import com.wuav.client.be.TicketType;
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