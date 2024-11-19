package com.bordercontrol;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create root container
            VBox root = new VBox(15);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.TOP_CENTER);
            root.setStyle("-fx-background-color: #f4f4f4;");

            // Create welcome label
            Label titleLabel = new Label("Border Control System");
            titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
            titleLabel.setStyle("-fx-text-fill: #2c3e50;");

            // Add buttons
            String[] menuItems = {
                "Register New Traveler",
                "View Traveler Information",
                "Edit Traveler Details",
                "Check Travel Eligibility",
                "Issue Visa/Permit",
                "Flag Traveler",
                "Record Entry/Exit",
                "Generate Reports",
                "Settings",
                "Exit"
            };

            root.getChildren().add(titleLabel);

            for (int i = 0; i < menuItems.length; i++) {
                Button button = createMenuButton(i + 1 + ". " + menuItems[i]);
                root.getChildren().add(button);
            }

            // Create scene
            Scene scene = new Scene(root, 600, 700);
            
            // Configure stage
            primaryStage.setTitle("Border Control System");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle("""
            -fx-background-color: #3498db;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10 20;
            -fx-min-width: 300;
            -fx-cursor: hand;
            """);
        
        button.setOnMouseEntered(e -> 
            button.setStyle("""
                -fx-background-color: #2980b9;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-padding: 10 20;
                -fx-min-width: 300;
                -fx-cursor: hand;
                """)
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle("""
                -fx-background-color: #3498db;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-padding: 10 20;
                -fx-min-width: 300;
                -fx-cursor: hand;
                """)
        );

        button.setOnAction(e -> handleMenuClick(text));
        return button;
    }

    private void handleMenuClick(String menuItem) {
        System.out.println("Selected: " + menuItem);
        // TODO: Implement menu actions
    }

    public static void main(String[] args) {
        launch(args);
    }
}