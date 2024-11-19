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

public class FlagTravelerView {
    
    public static void show(Stage parentStage, Traveler traveler) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.setTitle("Flag Traveler for Inspection");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Traveler Info
        Label travelerInfo = new Label(String.format("%s %s - Passport: %s",
            traveler.getFirstName(), 
            traveler.getLastName(), 
            traveler.getPassportNumber()));
        travelerInfo.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        // Flag Type
        form.add(new Label("Flag Type:"), 0, 0);
        ComboBox<String> flagTypeCombo = new ComboBox<>();
        flagTypeCombo.getItems().addAll(
            "SECURITY RISK",
            "DOCUMENT VERIFICATION",
            "CUSTOMS INSPECTION",
            "IMMIGRATION HOLD",
            "WATCH LIST",
            "OTHER"
        );
        flagTypeCombo.setPromptText("Select Flag Type");
        form.add(flagTypeCombo, 1, 0);

        // Reason
        form.add(new Label("Reason:"), 0, 1);
        TextArea reasonArea = new TextArea();
        reasonArea.setPrefRowCount(3);
        reasonArea.setWrapText(true);
        form.add(reasonArea, 1, 1);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button flagButton = new Button("Flag Traveler");
        flagButton.setStyle("""
            -fx-background-color: #e74c3c;
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

        buttonBox.getChildren().addAll(flagButton, cancelButton);

        // Flag button action
        flagButton.setOnAction(e -> {
            if (validateForm(flagTypeCombo, reasonArea)) {
                try {
                    FlaggedTraveler flag = new FlaggedTraveler(
                        traveler.getId(),
                        flagTypeCombo.getValue(),
                        reasonArea.getText()
                    );

                    FlaggedTravelerDAO dao = new FlaggedTravelerDAO();
                    dao.save(flag);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Traveler has been flagged for inspection");
                    alert.showAndWait();

                    stage.close();
                } catch (SQLException ex) {
                    showError("Error", "Could not flag traveler", ex.getMessage());
                }
            }
        });

        cancelButton.setOnAction(e -> stage.close());

        root.getChildren().addAll(travelerInfo, form, buttonBox);

        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private static boolean validateForm(ComboBox<String> flagTypeCombo, TextArea reasonArea) {
        StringBuilder errorMessage = new StringBuilder();

        if (flagTypeCombo.getValue() == null) {
            errorMessage.append("Please select a flag type\n");
        }
        if (reasonArea.getText().trim().isEmpty()) {
            errorMessage.append("Please provide a reason for flagging");
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