package com.bordercontrol.views;

import java.time.LocalDate;

import com.bordercontrol.models.Traveler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CheckEligibilityView {
    
    public static void show(Stage parentStage, Traveler traveler) {
        Stage stage = new Stage();
        stage.setTitle("Check Travel Eligibility");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Traveler Info
        Label infoLabel = new Label(String.format("Checking eligibility for: %s %s",
            traveler.getFirstName(), traveler.getLastName()));
        infoLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Results GridPane
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white; -fx-padding: 20;");

        int row = 0;

        // Passport Check
        grid.add(new Label("Passport Status:"), 0, row);
        boolean isPassportValid = traveler.getPassportExpiryDate().isAfter(LocalDate.now());
        Label passportLabel = new Label(isPassportValid ? "VALID" : "EXPIRED");
        passportLabel.setStyle(isPassportValid ? 
            "-fx-text-fill: green; -fx-font-weight: bold;" : 
            "-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(passportLabel, 1, row++);

        // Flag Check
        grid.add(new Label("Security Status:"), 0, row);
        Label flagLabel = new Label(traveler.isFlagged() ? "FLAGGED" : "CLEAR");
        flagLabel.setStyle(!traveler.isFlagged() ? 
            "-fx-text-fill: green; -fx-font-weight: bold;" : 
            "-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(flagLabel, 1, row++);

        // Overall Status
        grid.add(new Label("Travel Eligibility:"), 0, row);
        boolean isEligible = isPassportValid && !traveler.isFlagged();
        Label eligibilityLabel = new Label(isEligible ? "ELIGIBLE" : "NOT ELIGIBLE");
        eligibilityLabel.setStyle(isEligible ? 
            "-fx-text-fill: green; -fx-font-weight: bold;" : 
            "-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(eligibilityLabel, 1, row++);

        // Reason if not eligible
        if (!isEligible) {
            StringBuilder reason = new StringBuilder("Reason(s):\n");
            if (!isPassportValid) reason.append("- Passport has expired\n");
            if (traveler.isFlagged()) reason.append("- Traveler is flagged\n");
            
            TextArea reasonArea = new TextArea(reason.toString());
            reasonArea.setEditable(false);
            reasonArea.setPrefRowCount(3);
            reasonArea.setStyle("-fx-text-fill: red;");
            grid.add(reasonArea, 0, row++, 2, 1);
        }

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("""
            -fx-background-color: #3498db;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            """);
        closeButton.setOnAction(e -> stage.close());

        root.getChildren().addAll(infoLabel, grid, closeButton);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }
}