package com.bordercontrol.views;

import java.time.LocalDate;

import com.bordercontrol.models.Traveler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IssueVisaView {
    
    public static void show(Stage parentStage, Traveler traveler) {
        Stage stage = new Stage();
        stage.setTitle("Issue Visa/Permit");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white; -fx-padding: 20;");

        int row = 0;

        // Visa Type
        grid.add(new Label("Visa Type:"), 0, row);
        ComboBox<String> visaTypeCombo = new ComboBox<>();
        visaTypeCombo.getItems().addAll("Tourist", "Business", "Student", "Work", "Transit");
        grid.add(visaTypeCombo, 1, row++);

        // Duration
        grid.add(new Label("Duration (months):"), 0, row);
        ComboBox<Integer> durationCombo = new ComboBox<>();
        durationCombo.getItems().addAll(1, 3, 6, 12, 24);
        grid.add(durationCombo, 1, row++);

        // Start Date
        grid.add(new Label("Start Date:"), 0, row);
        DatePicker startDate = new DatePicker(LocalDate.now());
        grid.add(startDate, 1, row++);

        // Notes
        grid.add(new Label("Notes:"), 0, row);
        TextArea notes = new TextArea();
        notes.setPrefRowCount(3);
        grid.add(notes, 1, row++);

        // Buttons
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        Button issueButton = new Button("Issue Visa");
        issueButton.setStyle("""
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

        buttons.getChildren().addAll(issueButton, cancelButton);

        // Button Actions
        issueButton.setOnAction(e -> {
            if (validateForm(visaTypeCombo, durationCombo, startDate)) {
                showSuccess("Visa issued successfully!");
                stage.close();
            }
        });

        cancelButton.setOnAction(e -> stage.close());

        root.getChildren().addAll(
            new Label("Issue Visa for: " + traveler.getFirstName() + " " + traveler.getLastName()),
            grid,
            buttons
        );

        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private static boolean validateForm(ComboBox<String> visaType, 
                                     ComboBox<Integer> duration, 
                                     DatePicker startDate) {
        if (visaType.getValue() == null) {
            showError("Please select a visa type");
            return false;
        }
        if (duration.getValue() == null) {
            showError("Please select duration");
            return false;
        }
        if (startDate.getValue() == null) {
            showError("Please select start date");
            return false;
        }
        return true;
    }

    private static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}