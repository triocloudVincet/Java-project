package com.bordercontrol.views;

import java.sql.SQLException;

import com.bordercontrol.dao.FlaggedTravelerDAO;
import com.bordercontrol.models.FlaggedTraveler;
import com.bordercontrol.models.Traveler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FlagResolutionView {
    
    public static void show(Stage parentStage, Traveler traveler, FlaggedTraveler flag) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.setTitle("Resolve Flag");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Header
        Label headerLabel = new Label("Resolve Flag for " + traveler.getFirstName() + " " + traveler.getLastName());
        headerLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        // Flag Info
        GridPane flagInfo = new GridPane();
        flagInfo.setHgap(10);
        flagInfo.setVgap(10);
        flagInfo.setStyle("-fx-background-color: #fff; -fx-padding: 10; -fx-background-radius: 5;");

        flagInfo.add(new Label("Flag Type:"), 0, 0);
        flagInfo.add(new Label(flag.getFlagType()), 1, 0);
        
        flagInfo.add(new Label("Flag Date:"), 0, 1);
        flagInfo.add(new Label(flag.getFlagDate().format(
            java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))), 1, 1);
        
        flagInfo.add(new Label("Original Reason:"), 0, 2);
        TextArea originalReason = new TextArea(flag.getFlagReason());
        originalReason.setEditable(false);
        originalReason.setPrefRowCount(2);
        flagInfo.add(originalReason, 1, 2);

        // Resolution Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Resolution Status:"), 0, 0);
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("RESOLVED", "EXPIRED");
        statusCombo.setPromptText("Select Status");
        form.add(statusCombo, 1, 0);

        form.add(new Label("Resolution Notes:"), 0, 1);
        TextArea resolutionNotes = new TextArea();
        resolutionNotes.setPromptText("Enter resolution details...");
        resolutionNotes.setPrefRowCount(3);
        form.add(resolutionNotes, 1, 1);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button resolveButton = new Button("Resolve Flag");
        resolveButton.setStyle("""
            -fx-background-color: #27ae60;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """);

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("""
            -fx-background-color: #7f8c8d;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """);

        buttonBox.getChildren().addAll(resolveButton, cancelButton);

        // Button Actions
        resolveButton.setOnAction(e -> {
            if (validateForm(statusCombo, resolutionNotes)) {
                try {
                    FlaggedTravelerDAO dao = new FlaggedTravelerDAO();
                    dao.updateStatus(flag.getId(), 
                                   statusCombo.getValue(), 
                                   resolutionNotes.getText());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Flag has been resolved successfully");
                    alert.showAndWait();

                    stage.close();
                } catch (SQLException ex) {
                    showError("Error", "Could not resolve flag", ex.getMessage());
                }
            }
        });

        cancelButton.setOnAction(e -> stage.close());

        root.getChildren().addAll(headerLabel, flagInfo, form, buttonBox);

        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    private static boolean validateForm(ComboBox<String> statusCombo, TextArea resolutionNotes) {
        StringBuilder errorMessage = new StringBuilder();

        if (statusCombo.getValue() == null) {
            errorMessage.append("Please select a resolution status\n");
        }
        if (resolutionNotes.getText().trim().isEmpty()) {
            errorMessage.append("Please provide resolution notes");
        }

        if (errorMessage.length() > 0) {
            showError("Validation Error", "Please correct the following:", 
                     errorMessage.toString());
            return false;
        }
        return true;
    }

    private static void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}