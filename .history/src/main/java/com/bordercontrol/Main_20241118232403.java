package com.bordercontrol;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create main container
        VBox root = new VBox(20); // 20px spacing between elements
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Create title label
        Label titleLabel = new Label("Border Control System");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        // Create menu items
        Button[] menuButtons = {
            createButton("1. Register New Traveler"),
            createButton("2. View Traveler Information"),
            createButton("3. Edit Traveler Details"),
            createButton("4. Check Travel Eligibility"),
            createButton("5. Issue Visa/Permit"),
            createButton("6. Flag Traveler"),
            createButton("7. Record Entry/Exit"),
            createButton("8. Generate Reports"),
            createButton("9. Settings"),
            createButton("10. Exit")
        };

        // Add elements to root
        root.getChildren().add(titleLabel);
        for (Button button : menuButtons) {
            root.getChildren().add(button);
        }

        // Create scene and show
        Scene scene = new Scene(root, 600, 800);
        primaryStage.setTitle("Border Control System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14;");
        button.setPrefWidth(250);
        button.setOnAction(e -> handleMenuClick(text));
        return button;
    }

    private void handleMenuClick(String menuItem) {
        System.out.println("Clicked: " + menuItem);
        // TODO: Implement menu actions
    }

    public static void main(String[] args) {
        launch(args);
    }
}