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

public class TravelerRegistrationView {
    
    public static void show(Stage primaryStage) {
        // Create a new stage for registration
        Stage registrationStage = new Stage();
        
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
        
        // Personal Information Section
        Label personalInfoLabel = new Label("Personal Information");
        personalInfoLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        form.add(personalInfoLabel, 0, row++, 2, 1);

        form.add(new Label("First Name:"), 0, row);
        TextField firstNameField = new TextField();
        form.add(firstNameField, 1, row++);

        form.add(new Label("Last Name:"), 0, row);
        TextField lastNameField = new TextField();
        form.add(lastNameField, 1, row++);

        form.add(new Label("Date of Birth:"), 0, row);
        DatePicker dobPicker = new DatePicker();
        form.add(dobPicker, 1, row++);

        form.add(new Label("Nationality:"), 0, row);
        ComboBox<String> nationalityCombo = new ComboBox<>();
        nationalityCombo.getItems().addAll("Kenya", "Uganda", "Tanzania", "Rwanda", "Other");
        nationalityCombo.setPromptText("Select Nationality");
        form.add(nationalityCombo, 1, row++);

        // Travel Document Section
        row++;
        Label travelDocLabel = new Label("Travel Document Information");
        travelDocLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        form.add(travelDocLabel, 0, row++, 2, 1);

        form.add(new Label("Passport Number:"), 0, row);
        TextField passportField = new TextField();
        form.add(passportField, 1, row++);

        form.add(new Label("Issue Date:"), 0, row);
        DatePicker issueDatePicker = new DatePicker();
        form.add(issueDatePicker, 1, row++);

        form.add(new Label("Expiry Date:"), 0, row);
        DatePicker expiryDatePicker = new DatePicker();
        form.add(expiryDatePicker, 1, row++);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit");
        submitButton.setStyle("""
            -fx-background-color: #2ecc71;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """);

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("""
            -fx-background-color: #e74c3c;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """);

        buttonBox.getChildren().addAll(submitButton, cancelButton);

        // Add all components to root
        root.getChildren().addAll(titleLabel, form, buttonBox);

        // Button actions
        submitButton.setOnAction(e -> {
            // TODO: Implement submission logic
            System.out.println("Submitting registration for: " + 
                firstNameField.getText() + " " + lastNameField.getText());
            // Add validation and database storage here
            registrationStage.close();
        });

        cancelButton.setOnAction(e -> registrationStage.close());

        // Create and show scene
        Scene scene = new Scene(root, 500, 600);
        registrationStage.setTitle("Register New Traveler");
        registrationStage.setScene(scene);
        registrationStage.show();
    }
}