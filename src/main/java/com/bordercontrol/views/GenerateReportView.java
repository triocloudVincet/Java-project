package com.bordercontrol.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GenerateReportView {
    
    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("Generate Reports");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Report Types
        ComboBox<String> reportType = new ComboBox<>();
        reportType.getItems().addAll(
            "Daily Entry/Exit Summary",
            "Active Flags Report",
            "Visa Statistics",
            "Traveler Summary"
        );
        reportType.setPromptText("Select Report Type");
        reportType.setPrefWidth(200);

        Button generateButton = new Button("Generate Report");
        generateButton.setStyle("""
            -fx-background-color: #3498db;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            """);

        generateButton.setOnAction(e -> {
            if (reportType.getValue() != null) {
                showSuccess("Report generated successfully!\nCheck downloads folder.");
                stage.close();
            } else {
                showError("Please select a report type");
            }
        });

        root.getChildren().addAll(
            new Label("Select Report Type:"),
            reportType,
            generateButton
        );
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.show();
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