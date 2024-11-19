package com.bordercontrol.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.bordercontrol.models.Traveler;
import com.bordercontrol.dao.TravelerDAO;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class TravelerListView {
    
    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("View Travelers");
        
        // Create TableView
        TableView<Traveler> tableView = new TableView<>();
        
        // Create columns
        TableColumn<Traveler, String> passportCol = new TableColumn<>("Passport");
        passportCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getPassportNumber()
        ));
        
        TableColumn<Traveler, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getFirstName() + " " + data.getValue().getLastName()
        ));
        
        TableColumn<Traveler, String> nationalityCol = new TableColumn<>("Nationality");
        nationalityCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getNationality()
        ));
        
        TableColumn<Traveler, String> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        ));
        
        TableColumn<Traveler, String> expiryCol = new TableColumn<>("Passport Expiry");
        expiryCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getPassportExpiryDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        ));
        
        // Add columns to table
        tableView.getColumns().addAll(passportCol, nameCol, nationalityCol, dobCol, expiryCol);
        
        // Create search box
        TextField searchField = new TextField();
        searchField.setPromptText("Search by passport number or name...");
        searchField.setPrefWidth(250);
        
        Button searchButton = new Button("Search");
        searchButton.setStyle("""
            -fx-background-color: #3498db;
            -fx-text-fill: white;
            -fx-padding: 5 15;
            """);
        
        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("""
            -fx-background-color: #2ecc71;
            -fx-text-fill: white;
            -fx-padding: 5 15;
            """);
        
        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchField, searchButton, refreshButton);
        
        // Load data
        refreshButton.setOnAction(e -> loadTravelers(tableView));
        
        // Create layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(searchBox, tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        
        // Create scene
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        
        // Initial load
        loadTravelers(tableView);
        
        stage.show();
    }
    
    private static void loadTravelers(TableView<Traveler> tableView) {
        try {
            TravelerDAO dao = new TravelerDAO();
            tableView.setItems(FXCollections.observableArrayList(dao.findAll()));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load travelers");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}