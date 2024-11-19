package com.bordercontrol.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsView {
    
    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("Settings");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f4f4;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white; -fx-padding: 20;");

        int row = 0;

        // System Settings
        Label systemLabel = new Label("System Settings");
        systemLabel.setStyle("-fx-font-weight: bold;");
        grid.add(systemLabel, 0, row++, 2, 1);

        grid.add(new Label("Default Port:"), 0, row);
        ComboBox<String> portCombo = new ComboBox<>();
        portCombo.getItems().addAll(
            "Jomo Kenyatta International Airport",
            "Moi International Airport",
            "Busia Border",
            "Malaba Border"
        );
        grid.add(portCombo, 1, row++);

        // Save Button
        Button saveButton = new Button("Save Settings");
        saveButton.setStyle("""
            -fx-background-color: #2ecc71;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            """);

        saveButton.setOnAction(e -> {
            showSuccess("Settings saved successfully!");
            stage.close();
        });

        root.getChildren().addAll(grid, saveButton);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 500, 300);
        stage.setScene(scene);
        stage.show();
    }

    private static void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}