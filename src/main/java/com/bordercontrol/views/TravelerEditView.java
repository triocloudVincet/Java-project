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
import com.bordercontrol.dao.TravelerDAO;
import java.sql.SQLException;

public class TravelerEditView {
    
    public static void show(Stage parentStage, Traveler traveler) {
        Stage editStage = new Stage();
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(parentStage);
        editStage.setTitle("Edit Traveler Details");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Title
        Label titleLabel = new Label("Edit Traveler Information");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));

        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        int row = 0;

        // Personal Information
        Label personalInfoLabel = new Label("Personal Information");
        personalInfoLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        form.add(personalInfoLabel, 0, row++, 2, 1);

        // First Name
        form.add(new Label("First Name:"), 0, row);
        TextField firstNameField = new TextField(traveler.getFirstName());
        form.add(firstNameField, 1, row++);

        // Last Name
        form.add(new Label("Last Name:"), 0, row);
        TextField lastNameField = new TextField(traveler.getLastName());
        form.add(lastNameField, 1, row++);

        // Date of Birth
        form.add(new Label("Date of Birth:"), 0, row);
        DatePicker dobPicker = new DatePicker(traveler.getDateOfBirth());
        form.add(dobPicker, 1, row++);

        // Nationality
        form.add(new Label("Nationality:"), 0, row);
        ComboBox<String> nationalityCombo = new ComboBox<>();
        nationalityCombo.getItems().addAll("Kenya", "Uganda", "Tanzania", "Rwanda", "Other");
        nationalityCombo.setValue(traveler.getNationality());
        form.add(nationalityCombo, 1, row++);

        // Travel Document Information
        row++;
        Label travelDocLabel = new Label("Travel Document Information");
        travelDocLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        form.add(travelDocLabel, 0, row++, 2, 1);

        // Passport Number (Read-only)
        form.add(new Label("Passport Number:"), 0, row);
        TextField passportField = new TextField(traveler.getPassportNumber());
        passportField.setEditable(false);
        passportField.setStyle("-fx-background-color: #e8e8e8;");
        form.add(passportField, 1, row++);

        // Issue Date
        form.add(new Label("Issue Date:"), 0, row);
        DatePicker issueDatePicker = new DatePicker(traveler.getPassportIssueDate());
        form.add(issueDatePicker, 1, row++);

        // Expiry Date
        form.add(new Label("Expiry Date:"), 0, row);
        DatePicker expiryDatePicker = new DatePicker(traveler.getPassportExpiryDate());
        form.add(expiryDatePicker, 1, row++);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save Changes");
        saveButton.setStyle("""
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

        buttonBox.getChildren().addAll(saveButton, cancelButton);

        // Save button action
        saveButton.setOnAction(e -> {
            try {
                // Update traveler object
                traveler.setFirstName(firstNameField.getText());
                traveler.setLastName(lastNameField.getText());
                traveler.setDateOfBirth(dobPicker.getValue());
                traveler.setNationality(nationalityCombo.getValue());
                traveler.setPassportIssueDate(issueDatePicker.getValue());
                traveler.setPassportExpiryDate(expiryDatePicker.getValue());

                // Save to database
                TravelerDAO travelerDAO = new TravelerDAO();
                travelerDAO.update(traveler);

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Traveler information updated successfully!");
                alert.showAndWait();

                editStage.close();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Update Failed");
                alert.setContentText("Could not update traveler information: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        cancelButton.setOnAction(e -> editStage.close());

        root.getChildren().addAll(titleLabel, form, buttonBox);

        Scene scene = new Scene(root, 500, 600);
        editStage.setScene(scene);
        editStage.show();
    }
}