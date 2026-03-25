package com.pushpendra.prayasosappstore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.geometry.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Region;

public class HelloController {
    @FXML
    private ListView<App> appListView;

    private TerminalOutputDialog terminalDialog;
    private ObservableList<App> apps;

    @FXML
    public void initialize() {
        // Initialize the list of apps
        apps = FXCollections.observableArrayList();
        List<App> appList = new ArrayList<>();
        appList.add(new App("Firefox", "Mozilla Firefox web browser", "firefox"));
        appList.add(new App("VLC", "VLC media player", "vlc"));
        appList.add(new App("GIMP", "GNU Image Manipulation Program", "gimp"));
        appList.add(new App("LibreOffice", "LibreOffice office suite", "libreoffice"));
        appList.add(new App("Thunderbird", "Mozilla Thunderbird email client", "thunderbird"));
        appList.add(new App("Audacity", "Audio editor and recorder", "audacity"));
        appList.add(new App("Inkscape", "Vector graphics editor", "inkscape"));
        appList.add(new App("Blender", "3D creation suite", "blender"));
        appList.add(new App("Kdenlive", "Video editor", "kdenlive"));
        appList.add(new App("OBS Studio", "Screen recording and streaming", "obs-studio"));
        appList.add(new App("OBS Studio", "Screen recording and streaming", "obs-studio"));

        // Check installation status for each app
        for (App app : appList) {
            checkAppInstallationStatus(app);
        }

        apps.addAll(appList);
        appListView.setItems(apps);

        appListView.setCellFactory(lv -> {
            AppListCell cell = new AppListCell();
            cell.setPadding(new Insets(15));
            return cell;
        });

        // Configure ListView properties
        appListView.setStyle("-fx-background-color: transparent;");
        appListView.setFixedCellSize(175);

        // Ensure proper sizing for all items
        double totalHeight = (apps.size() * appListView.getFixedCellSize()) + 40; // Added extra padding
        appListView.setMinHeight(Math.min(totalHeight, 600)); // Cap at 600px

        // Add bottom padding to ensure last item is fully visible
        appListView.setPadding(new Insets(10, 10, 30, 10)); // Increased bottom padding

        // Initialize the terminal dialog
        terminalDialog = new TerminalOutputDialog();

        // Set up event handlers after the scene is ready
        appListView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setupEventHandlers();
            }
        });
    }

    public void setupEventHandlers() {
        if (appListView.getScene() != null && appListView.getScene().getWindow() != null) {
            appListView.getScene().getWindow().addEventHandler(InstallAppEvent.INSTALL_APP, this::handleAppInstallation);
            appListView.getScene().getWindow().addEventHandler(RemoveAppEvent.REMOVE_APP, this::handleAppRemoval);
        }
    }

    private void checkAppInstallationStatus(App app) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("pacman", "-Qi", app.getPackageName());
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            app.setInstalled(exitCode == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            app.setInstalled(false);
        }
    }

    private void handleAppInstallation(InstallAppEvent event) {
        String packageName = event.getPackageName();
        terminalDialog.show();
        terminalDialog.clearOutput(); // Clear previous output
        terminalDialog.appendOutput("Installing " + packageName + "...\n");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("pkexec", "pacman", "-S", "--noconfirm", packageName);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the output in a separate thread
            Thread outputThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String finalLine = line;
                        javafx.application.Platform.runLater(() -> {
                            terminalDialog.appendOutput(finalLine + "\n");
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputThread.start();

            // Monitor process completion in another thread
            new Thread(() -> {
                try {
                    int exitCode = process.waitFor();
                    javafx.application.Platform.runLater(() -> {
                        if (exitCode == 0) {
                            terminalDialog.appendOutput("\nInstallation completed successfully!");
                            terminalDialog.setInstallationStatus(true);
                            // Update app installation status
                            for (App app : apps) {
                                if (app.getPackageName().equals(packageName)) {
                                    app.setInstalled(true);
                                    break;
                                }
                            }
                        } else {
                            terminalDialog.appendOutput("\nInstallation failed with exit code: " + exitCode);
                            terminalDialog.setInstallationStatus(false);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    javafx.application.Platform.runLater(() -> {
                        terminalDialog.appendOutput("\nInstallation process was interrupted!");
                        terminalDialog.setInstallationStatus(false);
                    });
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                terminalDialog.appendOutput("\nError starting installation process: " + e.getMessage());
                terminalDialog.setInstallationStatus(false);
            });
        }
    }

    private void handleAppRemoval(RemoveAppEvent event) {
        String packageName = event.getPackageName();
        terminalDialog.show();
        terminalDialog.clearOutput();
        terminalDialog.appendOutput("Removing " + packageName + "...\n");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("pkexec", "pacman", "-R", "--noconfirm", packageName);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the output in a separate thread
            Thread outputThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String finalLine = line;
                        javafx.application.Platform.runLater(() -> {
                            terminalDialog.appendOutput(finalLine + "\n");
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputThread.start();

            // Monitor process completion in another thread
            new Thread(() -> {
                try {
                    int exitCode = process.waitFor();
                    javafx.application.Platform.runLater(() -> {
                        if (exitCode == 0) {
                            terminalDialog.appendOutput("\nRemoval completed successfully!");
                            terminalDialog.setInstallationStatus(true);
                            // Update app installation status
                            for (App app : apps) {
                                if (app.getPackageName().equals(packageName)) {
                                    app.setInstalled(false);
                                    break;
                                }
                            }
                        } else {
                            terminalDialog.appendOutput("\nRemoval failed with exit code: " + exitCode);
                            terminalDialog.setInstallationStatus(false);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    javafx.application.Platform.runLater(() -> {
                        terminalDialog.appendOutput("\nRemoval process was interrupted!");
                        terminalDialog.setInstallationStatus(false);
                    });
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                terminalDialog.appendOutput("\nError starting removal process: " + e.getMessage());
                terminalDialog.setInstallationStatus(false);
            });
        }
    }
}
