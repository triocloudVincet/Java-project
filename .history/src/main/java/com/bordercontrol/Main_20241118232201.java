package com.bordercontrol;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create root container
            VBox root = new VBox(10);
            root.setStyle("-fx-padding: 20; -fx-alignment: center;");

            // Create welcome label
            Label welcomeLabel = new Label("Welcome to Border Control System");
            welcomeLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

            // Create menu button
            Button menuButton = new Button("Open Menu");
            menuButton.setStyle("-fx-font-size: 16;");
            menuButton.setOnAction(e -> showMenu());

            // Add components to root
            root.getChildren().addAll(welcomeLabel, menuButton);

            // Create scene
            Scene scene = new Scene(root, 600, 400);
            
            // Configure stage
            primaryStage.setTitle("Border Control System");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Add close request handler
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    private void showMenu() {
        // TODO: Implement menu functionality
        System.out.println("Menu button clicked");
    }

    public static void main(String[] args) {
        launch(args);
    }
}