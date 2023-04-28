package com.event_bar_easv.bll.services;

import com.event_bar_easv.be.Event;
import com.event_bar_easv.be.SpecialTicketType;
import com.event_bar_easv.be.TicketType;
import com.event_bar_easv.bll.services.interfaces.IEventService;
import com.event_bar_easv.dal.interfaces.IEventRepository;
import com.google.inject.Inject;

import java.sql.Date;
import java.util.List;

public class EventService implements IEventService {

    private final IEventRepository eventRepository;

    @Inject
    public EventService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }


    @Override
    public List<SpecialTicketType> getAllSpecialTickets() {
        return eventRepository.getAllSpecialTickets();
    }

    @Override
    public int addSpecialTicketToAllEvent(SpecialTicketType specialTicketType, List<Integer> collectedIds) {
        int finalResult = 0;
        for (Integer id : collectedIds) {
            finalResult =  eventRepository.addSpecialTicketToEvent(specialTicketType, id);
        }
        return finalResult;
    }

    @Override
    public int addSpecialTicketToEvent(SpecialTicketType specialTicketType, int eventId) {
        return eventRepository.addSpecialTicketToEvent(specialTicketType, eventId);
    }

    @Override
    public int createSpecialTicket(SpecialTicketType specialTicketType) {
        return eventRepository.createSpecialTicket(specialTicketType);
    }

    @Override
    public int createEvent(Event event) {
        var id = event.getEventId();
        var title = event.getEventName();
        var loc = event.getLocation();
        var startDate2 =  event.getStartDate();
        var endDate2 = event.getEndDate();
        var startTime2 = event.getStartTime();
        var endTime2 = event.getEndTime();
        var notes2 = event.getDescription();

        // create event

        var createdEvent = eventRepository.createEvent(
                id,
                title,
                loc,
                startDate2,
                endDate2,
                startTime2,
                endTime2,
                notes2
        );


        if(createdEvent > 0 ){
            int almostResult = 0;
            for (TicketType ticketType : event.getTicketTypes()
                 ) {
                almostResult =  eventRepository.createTicketType(ticketType);

            }
            System.out.println(almostResult);
            System.out.println("All types created ");

            if(almostResult > 0){
                int finalResult = 0;
                for (TicketType ticketType : event.getTicketTypes()
                ) {
                    finalResult =  eventRepository.addTypeToEvent(id,ticketType);
                }
                if(finalResult > 0){
                    return finalResult;
                }
            }
        }

//
//        if(createdEvent > 0){
//            int finalResult = 0;
//            for (TicketType ticketType : event.getTicketTypes()
//                 ) {
//                finalResult =  eventRepository.addTypeToEvent(id,ticketType);
//
//            }
//            if(finalResult > 0){
//                return finalResult;
//            }
//        }

        return createdEvent;

    }


}
