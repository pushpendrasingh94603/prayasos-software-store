package com.pushpendra.prayasosappstore;

import javafx.event.Event;
import javafx.event.EventType;

public class RemoveAppEvent extends Event {
    public static final EventType<RemoveAppEvent> REMOVE_APP = new EventType<>(Event.ANY, "REMOVE_APP");
    private final String packageName;

    public RemoveAppEvent(String packageName) {
        super(REMOVE_APP);
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
} 