package com.wuav.client.gui.utils.event;

import com.wuav.client.bll.helpers.EventType;

/**
 * The type Custom event.
 */
public class CustomEvent {
    private EventType eventType;
    private Object data;

    private String message;

    /**
     * Instantiates a new Custom event.
     *
     * @param eventType the event type
     * @param data      the data
     */
    public CustomEvent(EventType eventType, Object data, String message) {
        this.eventType = eventType;
        this.data = data;
        this.message = message;
    }

    /**
     * Gets event type.
     *
     * @return the event type
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
