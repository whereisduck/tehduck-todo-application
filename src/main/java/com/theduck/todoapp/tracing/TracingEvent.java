package com.theduck.todoapp.tracing;

import org.springframework.context.ApplicationEvent;

public class TracingEvent extends ApplicationEvent {
    private final String uri;
    private final String username;
    public TracingEvent(Object indexController, String index, String s) {
        super(indexController);
        this.uri = index;
        this.username = s;
    }

    public String getUri() {
        return uri;
    }

    public String getUsername() {
        return username;
    }
}
