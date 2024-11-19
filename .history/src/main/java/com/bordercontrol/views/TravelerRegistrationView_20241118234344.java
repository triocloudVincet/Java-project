package com.bordercontrol.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class TravelerRegistrationView {
    
    public static void show(Stage parentStage) {
        System.out.println("Opening registration window...");
        
        // Create a new stage for registration
        Stage registrationStage = new Stage();
        registrationStage.initModality(Modality.WINDOW_MODAL);
        registrationStage.initOwner(parentStage);
        
        // Create the root container
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Title
        Label titleLabel = new Label("Traveler Registration");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        // Create form grid
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        // Add form fields
        int row = 0;
        
        form.add(new Label("First Name:"), 0, row);
        TextField firstNameField = new TextField();
        form.add(firstNameField, 1, row++);

        form.add(new Label("Last Name:"), 0, row);
        TextField lastNameField = new TextField();
        form.add(lastNameField, 1, row++);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit");
        submitButton.setStyle("""
            -fx-background-color: #2ecc71;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            """);

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("""
            -fx-background-color: #e74c3c;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            """);

        buttonBox.getChildren().addAll(submitButton, cancelButton);

        // Add all components to root
        root.getChildren().addAll(titleLabel, form, buttonBox);

        // Button actions
        submitButton.setOnAction(e -> {
            System.out.println("Submitting registration for: " + 
                firstNameField.getText() + " " + lastNameField.getText());
            registrationStage.close();
        });

        cancelButton.setOnAction(e -> registrationStage.close());

        // Create and show scene
        Scene scene = new Scene(root, 400, 300);
        registrationStage.setTitle("Register New Traveler");
        registrationStage.setScene(scene);
        registrationStage.show();
    }
}