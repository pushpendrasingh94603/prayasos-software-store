package com.pushpendra.prayasosappstore;

import javafx.event.Event;
import javafx.event.EventType;

public class InstallAppEvent extends Event {
    public static final EventType<InstallAppEvent> INSTALL_APP = new EventType<>(Event.ANY, "INSTALL_APP");
    private final String packageName;

    public InstallAppEvent(String packageName) {
        super(INSTALL_APP);
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
} 