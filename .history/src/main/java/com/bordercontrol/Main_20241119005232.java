package com.bordercontrol;

import com.bordercontrol.utils.DatabaseUtil;
import com.bordercontrol.views.TravelerListView;
import com.bordercontrol.views.TravelerRegistrationView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage primaryStage;

    @Override
    public void init() throws Exception {
        super.init();
        // Initialize database
        DatabaseUtil.createTables();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        System.out.println("Application starting...");
        try {
            // Create root container
            VBox root = new VBox(15);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.TOP_CENTER);
            root.setStyle("-fx-background-color: #f4f4f4;");

            System.out.println("Creating UI components...");

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

            System.out.println("Setting up scene...");

            // Create scene
            Scene scene = new Scene(root, 600, 700);
            
            // Configure stage
            primaryStage.setTitle("Border Control System");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            System.out.println("Application window should now be visible");
            
        } catch (Exception e) {
            System.err.println("Error starting application: ");
            e.printStackTrace();
        }
    }

    private Button createMenuButton(String text) {
        System.out.println("Creating button: " + text);
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
    System.out.println("Button clicked: " + menuItem);
    
    if (menuItem.contains("Register New Traveler")) {
        TravelerRegistrationView.show(primaryStage);
    } else if (menuItem.contains("View Traveler")) {
        TravelerListView.show(primaryStage);
    } else if (menuItem.contains("Exit")) {
        System.out.println("Exiting application...");
        primaryStage.close();
    }
}

    public static void main(String[] args) {
        System.out.println("Starting main method...");
        launch(args);
    }
}