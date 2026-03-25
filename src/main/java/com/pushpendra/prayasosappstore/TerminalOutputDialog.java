package com.pushpendra.prayasosappstore;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class TerminalOutputDialog extends Dialog<Void> {
    private TextArea outputArea;
    private Button closeButton;
    private boolean installationSuccessful;

    public TerminalOutputDialog() {
        setTitle("Installation Progress");
        initModality(Modality.APPLICATION_MODAL);

        // Set dialog theme
        getDialogPane().setStyle("-fx-background-color: #E0F2F1;");

        // Create the output area with theme
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefRowCount(20);
        outputArea.setPrefColumnCount(60);
        outputArea.setWrapText(true);
        outputArea.setStyle(
                "-fx-font-family: 'monospace';" +
                        "-fx-background-color: #FFFFFF;" +
                        "-fx-control-inner-background: #FFFFFF;" +
                        "-fx-border-color: #B2DFDB;" +
                        "-fx-border-width: 1px;"
        );

        // Create close button with theme
        closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            getDialogPane().getScene().getWindow().hide();
        });
        closeButton.setDisable(true);
        closeButton.setStyle(
                "-fx-background-color: #009688;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 4;"
        );

        // Create button container
        HBox buttonBox = new HBox(closeButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        // Create main container
        VBox content = new VBox(10);
        content.getChildren().addAll(outputArea, buttonBox);
        content.setPadding(new Insets(15));
        content.setStyle("-fx-background-color: #E0F2F1;");

        getDialogPane().setContent(content);
        getDialogPane().setPrefWidth(700);
        getDialogPane().setPrefHeight(500);

        // Set the stage icon
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pushpendra/prayasosappstore/icon.png")));

        // Add hover effect to close button
        closeButton.setOnMouseEntered(e -> {
            if (!closeButton.isDisabled()) {
                closeButton.setStyle(
                        "-fx-background-color: #00796B;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 8 16;" +
                                "-fx-background-radius: 4;"
                );
            }
        });

        closeButton.setOnMouseExited(e -> {
            if (!closeButton.isDisabled()) {
                closeButton.setStyle(
                        "-fx-background-color: #009688;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 8 16;" +
                                "-fx-background-radius: 4;"
                );
            }
        });
    }

    public void appendOutput(String text) {
        outputArea.appendText(text);
        // Auto-scroll to bottom
        outputArea.setScrollTop(Double.MAX_VALUE);
    }

    public void clearOutput() {
        outputArea.clear();
        installationSuccessful = false;
        closeButton.setDisable(true);
        closeButton.setStyle(
                "-fx-background-color: #009688;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 4;"
        );
    }

    public void setInstallationStatus(boolean successful) {
        installationSuccessful = successful;
        closeButton.setDisable(false);

        // Update button style based on status
        if (successful) {
            closeButton.setStyle(
                    "-fx-background-color: #00796B;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 8 16;" +
                            "-fx-background-radius: 4;"
            );
        } else {
            closeButton.setStyle(
                    "-fx-background-color: #D32F2F;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 8 16;" +
                            "-fx-background-radius: 4;"
            );
        }
    }

    public boolean isInstallationSuccessful() {
        return installationSuccessful;
    }
}