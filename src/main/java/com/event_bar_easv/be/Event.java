package com.event_bar_easv.be;

import com.event_bar_easv.be.user.AppUser;

import java.sql.Date;
import java.util.List;

public class Event {
    private int eventId;
    private String eventName;
    private Date startDate;
    private Date endDate;
    private String location;
    private String description;
    private Date createdAt;

    private List<AppUser> participants;

    private String startTime;

    private String endTime;

    private List<TicketType> ticketTypes;

    private List<SpecialTicketType> specialTicketTypes;




    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<AppUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<AppUser> participants) {
        this.participants = participants;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public List<TicketType> getTicketTypes() {
        return ticketTypes;
    }



    public void setTicketTypes(List<TicketType> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }


    public List<SpecialTicketType> getSpecialTicketTypes() {
        return specialTicketTypes;
    }

    public void setSpecialTicketTypes(List<SpecialTicketType> specialTicketTypes) {
        this.specialTicketTypes = specialTicketTypes;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", participants=" + participants +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", ticketTypes=" + ticketTypes +
                ", specialTicketTypes=" + specialTicketTypes +
                '}';
    }
}
