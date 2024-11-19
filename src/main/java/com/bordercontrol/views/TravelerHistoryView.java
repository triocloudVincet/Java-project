package com.bordercontrol.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.layout.Priority;
import javafx.collections.FXCollections;
import com.bordercontrol.models.Traveler;
import com.bordercontrol.models.EntryExitRecord;
import com.bordercontrol.dao.EntryExitDAO;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class TravelerHistoryView {
    
    public static void show(Stage parentStage, Traveler traveler) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.setTitle("Travel History - " + traveler.getFirstName() + " " + traveler.getLastName());

        // Create TableView
        TableView<EntryExitRecord> tableView = new TableView<>();
        
        // Create columns
        TableColumn<EntryExitRecord, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getRecordType()
        ));
        typeCol.setPrefWidth(80);
        
        TableColumn<EntryExitRecord, String> dateCol = new TableColumn<>("Date/Time");
        dateCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getRecordTime().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        ));
        dateCol.setPrefWidth(150);
        
        TableColumn<EntryExitRecord, String> portCol = new TableColumn<>("Port");
        portCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getPortOfEntry()
        ));
        portCol.setPrefWidth(200);
        
        TableColumn<EntryExitRecord, String> purposeCol = new TableColumn<>("Purpose");
        purposeCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getPurposeOfTravel()
        ));
        purposeCol.setPrefWidth(120);
        
        TableColumn<EntryExitRecord, String> visaCol = new TableColumn<>("Visa");
        visaCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getVisaNumber()
        ));
        visaCol.setPrefWidth(100);
        
        TableColumn<EntryExitRecord, String> remarksCol = new TableColumn<>("Remarks");
        remarksCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getRemarks()
        ));
        remarksCol.setPrefWidth(200);

        // Add style to ENTRY/EXIT rows
        tableView.setRowFactory(tv -> new TableRow<EntryExitRecord>() {
            @Override
            protected void updateItem(EntryExitRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getRecordType().equals("ENTRY")) {
                    setStyle("-fx-background-color: #d5f5e3;"); // Light green for entry
                } else {
                    setStyle("-fx-background-color: #fadbd8;"); // Light red for exit
                }
            }
        });

        // Add columns to table
        tableView.getColumns().addAll(typeCol, dateCol, portCol, purposeCol, visaCol, remarksCol);
        
        // Load data
        try {
            EntryExitDAO dao = new EntryExitDAO();
            tableView.setItems(FXCollections.observableArrayList(
                dao.findByTravelerId(traveler.getId())
            ));
        } catch (SQLException e) {
            showError("Load Error", "Could not load travel history", e.getMessage());
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