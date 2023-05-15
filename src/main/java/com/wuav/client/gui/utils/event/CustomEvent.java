package com.wuav.client.gui.utils.event;

import com.wuav.client.bll.helpers.EventType;

public class CustomEvent {
    private EventType eventType;
    private Object data;

    public CustomEvent(EventType eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Object getData() {
        return data;
    }
}
