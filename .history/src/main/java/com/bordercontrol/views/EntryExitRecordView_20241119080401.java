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
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.bordercontrol.models.Traveler;
import com.bordercontrol.models.EntryExitRecord;
import com.bordercontrol.dao.EntryExitDAO;
import java.sql.SQLException;

public class EntryExitRecordView {
    
    public static void show(Stage parentStage, Traveler traveler) {
        Stage recordStage = new Stage();
        recordStage.initModality(Modality.WINDOW_MODAL);
        recordStage.initOwner(parentStage);
        recordStage.setTitle("Record Entry/Exit");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Traveler Info Display
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

        int row = 0;

        // Record Type
        form.add(new Label("Record Type:"), 0, row);
        ComboBox<String> recordTypeCombo = new ComboBox<>();
        recordTypeCombo.getItems().addAll("ENTRY", "EXIT");
        recordTypeCombo.setPromptText("Select Type");
        form.add(recordTypeCombo, 1, row++);

        // Port of Entry
        form.add(new Label("Port of Entry:"), 0, row);
        ComboBox<String> portCombo = new ComboBox<>();
        portCombo.getItems().addAll(
            "Jomo Kenyatta International Airport",
            "Moi International Airport",
            "Busia Border",
            "Malaba Border",
            "Namanga Border"
        );
        portCombo.setPromptText("Select Port");
        form.add(portCombo, 1, row++);

        // Purpose of Travel
        form.add(new Label("Purpose of Travel:"), 0, row);
        ComboBox<String> purposeCombo = new ComboBox<>();
        purposeCombo.getItems().addAll(
            "Tourism",
            "Business",
            "Education",
            "Employment",
            "Medical",
            "Other"
        );
        purposeCombo.setPromptText("Select Purpose");
        form.add(purposeCombo, 1, row++);

        // Visa Number
        form.add(new Label("Visa Number:"), 0, row);
        TextField visaField = new TextField();
        form.add(visaField, 1, row++);

        // Remarks
        form.add(new Label("Remarks:"), 0, row);
        TextArea remarksArea = new TextArea();
        remarksArea.setPrefRowCount(3);
        form.add(remarksArea, 1, row++);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button recordButton = new Button("Record");
        recordButton.setStyle("""
            -fx-background-color: #2ecc71;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """);

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("""
            -fx-background-color: #e74c3c;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """);

        buttonBox.getChildren().addAll(recordButton, cancelButton);

        // Record button action
        recordButton.setOnAction(e -> {
            if (validateForm(recordTypeCombo, portCombo, purposeCombo)) {
                try {
                    EntryExitRecord record = new EntryExitRecord(
                        traveler.getId(),
                        recordTypeCombo.getValue(),
                        portCombo.getValue(),
                        purposeCombo.getValue(),
                        visaField.getText(),
                        remarksArea.getText()
                    );

                    EntryExitDAO dao = new EntryExitDAO();
                    dao.save(record);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText(record.getRecordType() + " recorded successfully!");
                    alert.showAndWait();

                    recordStage.close();
                } catch (SQLException ex) {
                    showError("Record Error", "Could not save record", ex.getMessage());
                }
            }
        });

        cancelButton.setOnAction(e -> recordStage.close());

        root.getChildren().addAll(travelerInfo, form, buttonBox);

        Scene scene = new Scene(root, 500, 500);
        recordStage.setScene(scene);
        recordStage.show();
    }

    private static boolean validateForm(ComboBox<String> recordTypeCombo,
                                      ComboBox<String> portCombo,
                                      ComboBox<String> purposeCombo) {
        StringBuilder errorMessage = new StringBuilder();

        if (recordTypeCombo.getValue() == null) {
            errorMessage.append("Record type must be selected\n");
        }
        if (portCombo.getValue() == null) {
            errorMessage.append("Port of entry must be selected\n");
        }
        if (purposeCombo.getValue() == null) {
            errorMessage.append("Purpose of travel must be selected\n");
        }

        if (errorMessage.length() > 0) {
            showError("Validation Error", "Please correct the following errors:", 
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