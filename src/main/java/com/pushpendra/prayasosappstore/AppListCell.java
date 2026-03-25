package com.pushpendra.prayasosappstore;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import com.pushpendra.prayasosappstore.RemoveAppEvent;
import com.pushpendra.prayasosappstore.InstallAppEvent;

public class AppListCell extends ListCell<App> {
    private VBox content;
    private Label nameLabel;
    private Text descriptionText;
    private Button actionButton;
    private HBox mainContainer;

    public AppListCell() {
        super();
        nameLabel = new Label();
        nameLabel.getStyleClass().add("app-name");

        descriptionText = new Text();
        descriptionText.getStyleClass().add("app-description");
        descriptionText.setWrappingWidth(750);

        actionButton = new Button("Install");
        actionButton.getStyleClass().add("install-button");

        // Create a VBox for the text content
        VBox textContent = new VBox(10);
        textContent.setAlignment(Pos.TOP_LEFT);
        textContent.getChildren().addAll(nameLabel, descriptionText);

        // Create main container to hold text and button
        mainContainer = new HBox(20);
        mainContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textContent, Priority.ALWAYS);
        mainContainer.getChildren().addAll(textContent, actionButton);

        content = new VBox();
        content.setPadding(new Insets(15));
        content.getChildren().add(mainContainer);
        content.setMaxWidth(Double.MAX_VALUE);

        // Set minimum height for the cell
        setMinHeight(120);
        setPrefHeight(120);
    }

    @Override
    protected void updateItem(App app, boolean empty) {
        super.updateItem(app, empty);
        if (empty) {
            setGraphic(null);
            setText(null);
        } else {
            nameLabel.setText(app.getName());
            descriptionText.setText(app.getDescription());

            // Update button text and action based on installation status
            if (app.isInstalled()) {
                actionButton.setText("Remove");
                actionButton.getStyleClass().setAll("install-button", "remove-button");
                actionButton.setOnAction(e -> {
                    if (getScene() != null && getScene().getWindow() != null) {
                        getScene().getWindow().fireEvent(
                                new RemoveAppEvent(app.getPackageName())
                        );
                    }
                });
            } else {
                actionButton.setText("Install");
                actionButton.getStyleClass().setAll("install-button");
                actionButton.setOnAction(e -> {
                    if (getScene() != null && getScene().getWindow() != null) {
                        getScene().getWindow().fireEvent(
                                new InstallAppEvent(app.getPackageName())
                        );
                    }
                });
            }

            setGraphic(content);
            setText(null);
        }
    }
}