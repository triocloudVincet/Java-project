package com.bordercontrol.views;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import com.bordercontrol.dao.TravelerDAO;
import com.bordercontrol.models.Traveler;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        passportCol.setPrefWidth(120);
        
        TableColumn<Traveler, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getFirstName() + " " + data.getValue().getLastName()
        ));
        nameCol.setPrefWidth(150);
        
        TableColumn<Traveler, String> nationalityCol = new TableColumn<>("Nationality");
        nationalityCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getNationality()
        ));
        nationalityCol.setPrefWidth(100);
        
        TableColumn<Traveler, String> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        ));
        dobCol.setPrefWidth(100);
        
        TableColumn<Traveler, String> expiryCol = new TableColumn<>("Passport Expiry");
        expiryCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
            () -> data.getValue().getPassportExpiryDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        ));
        expiryCol.setPrefWidth(100);

        // Add edit button column
        TableColumn<Traveler, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(100);
        actionCol.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            {
                editButton.setStyle("""
                    -fx-background-color: #f39c12;
                    -fx-text-fill: white;
                    -fx-cursor: hand;
                    """);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    editButton.setOnAction(event -> {
                        Traveler traveler = getTableView().getItems().get(getIndex());
                        TravelerEditView.show(stage, traveler);
                        // Refresh table after edit
                        loadTravelers(tableView);
                    });
                    setGraphic(editButton);
                }
            }
        });
        
        // Add columns to table
        tableView.getColumns().addAll(passportCol, nameCol, nationalityCol, dobCol, expiryCol, actionCol);
        
        // Create search box
        TextField searchField = new TextField();
        searchField.setPromptText("Search by passport number or name...");
        searchField.setPrefWidth(250);
        
        Button searchButton = new Button("Search");
        searchButton.setStyle("""
            -fx-background-color: #3498db;
            -fx-text-fill: white;
            -fx-padding: 5 15;
            -fx-cursor: hand;
            """);
        
        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("""
            -fx-background-color: #2ecc71;
            -fx-text-fill: white;
            -fx-padding: 5 15;
            -fx-cursor: hand;
            """);
        
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.getChildren().addAll(searchField, searchButton, refreshButton);
        
        // Search functionality
        searchButton.setOnAction(e -> {
            try {
                TravelerDAO dao = new TravelerDAO();
                String searchTerm = searchField.getText().trim();
                if (!searchTerm.isEmpty()) {
                    tableView.setItems(FXCollections.observableArrayList(
                        dao.searchTravelers(searchTerm)
                    ));
                } else {
                    loadTravelers(tableView);
                }
            } catch (SQLException ex) {
                showError("Search Error", "Could not perform search", ex.getMessage());
            }
        });
        
        // Refresh functionality
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
            showError("Load Error", "Could not load travelers", e.getMessage());
        }
    }
    
    private static void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}