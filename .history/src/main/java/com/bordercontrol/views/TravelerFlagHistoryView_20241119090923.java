package com.bordercontrol.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.collections.FXCollections;
import com.bordercontrol.models.Traveler;
import com.bordercontrol.models.FlaggedTraveler;
import com.bordercontrol.dao.FlaggedTravelerDAO;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class TravelerFlagHistoryView {
    
    public static void show(Stage parentStage, Traveler traveler) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.setTitle("Flag History - " + traveler.getFirstName() + " " + traveler.getLastName());

        // Create TableView
        TableView<FlaggedTraveler> tableView = new TableView<>();
        
        // Create columns
        TableColumn<FlaggedTraveler, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getStatus()
        ));
        statusCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "ACTIVE":
                            setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
                            break;
                        case "RESOLVED":
                            setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                            break;
                        case "EXPIRED":
                            setStyle("-fx-text-fill: #7f8c8d; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        statusCol.setPrefWidth(100);

        TableColumn<FlaggedTraveler, String> typeCol = new TableColumn<>("Flag Type");
        typeCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getFlagType()
        ));
        typeCol.setPrefWidth(150);
        
        TableColumn<FlaggedTraveler, String> dateCol = new TableColumn<>("Flag Date");
        dateCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getFlagDate().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        ));
        dateCol.setPrefWidth(150);
        
        TableColumn<FlaggedTraveler, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getFlagReason()
        ));
        reasonCol.setPrefWidth(200);

        TableColumn<FlaggedTraveler, String> resolutionCol = new TableColumn<>("Resolution");
        resolutionCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> {
                if (data.getValue().getResolutionNotes() != null) {
                    return data.getValue().getResolutionNotes() + 
                           "\nResolved by: " + data.getValue().getResolvedBy() +
                           " on " + data.getValue().getResolvedDate().format(
                               DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                }
                return "";
            }
        ));
        resolutionCol.setPrefWidth(250);

        // Add columns to table
        tableView.getColumns().addAll(statusCol, typeCol, dateCol, reasonCol, resolutionCol);

        // Add row coloring
        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(FlaggedTraveler item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if ("ACTIVE".equals(item.getStatus())) {
                    setStyle("-fx-background-color: #ffebee;"); // Light red for active flags
                } else if ("RESOLVED".equals(item.getStatus())) {
                    setStyle("-fx-background-color: #e8f5e9;"); // Light green for resolved
                } else {
                    setStyle("-fx-background-color: #f5f5f5;"); // Light grey for expired
                }
            }
        });
        
        // Load data
        try {
            FlaggedTravelerDAO dao = new FlaggedTravelerDAO();
            tableView.setItems(FXCollections.observableArrayList(
                dao.findByTravelerId(traveler.getId())
            ));
        } catch (SQLException e) {
            showError("Load Error", "Could not load flag history", e.getMessage());
        }

        // Summary section
        Label summaryLabel = new Label(String.format("""
            Traveler: %s %s
            Passport: %s
            Nationality: %s
            """,
            traveler.getFirstName(),
            traveler.getLastName(),
            traveler.getPassportNumber(),
            traveler.getNationality()
        ));
        summaryLabel.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");

        // Create layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(summaryLabel, tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        
        // Create scene
        Scene scene = new Scene(layout, 900, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private static void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}