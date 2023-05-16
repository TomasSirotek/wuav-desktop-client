package com.wuav.client.gui.utils.event;

import com.wuav.client.bll.helpers.EventType;

public class CustomEvent {
    private EventType eventType;
    private Object data;

    private String message;

    public CustomEvent(EventType eventType, Object data, String message) {
        this.eventType = eventType;
        this.data = data;
        this.message = message;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
