package com.wuav.client.gui.utils;


import com.wuav.client.be.Project;
import com.wuav.client.bll.helpers.EventType;

public class ProjectEvent {
    private final EventType eventType;
    private final Project project;

    public ProjectEvent(EventType eventType, Project project) {
        this.eventType = eventType;
        this.project = project;
    }

    public EventType eventType() {
        return eventType;
    }

    public Project getProject() {
        return project;
    }
}