package com.pushpendra.prayasosappstore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("mycssstyle.css").toExternalForm());
        stage.setTitle("prayasOS-AppStore");

        Image icon = new Image(getClass().getResource("icon.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setScene(scene);

        // Setup event handlers after scene is created
        HelloController controller = fxmlLoader.getController();
        controller.setupEventHandlers();
        // stage.setHeight(290);
        // stage.setWidth(1080);
        // stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}