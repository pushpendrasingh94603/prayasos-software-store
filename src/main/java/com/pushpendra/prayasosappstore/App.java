package com.pushpendra.prayasosappstore;

public class App {
    private String name;
    private String description;
    private String packageName;
    private boolean isInstalled;

    public App(String name, String description, String packageName) {
        this.name = name;
        this.description = description;
        this.packageName = packageName;
        this.isInstalled = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }
} 