package com.bordercontrol.views;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.bordercontrol.dao.TravelerDAO;
import com.bordercontrol.models.Traveler;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

        // Add actions column
        TableColumn<Traveler, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(200);
        actionCol.setCellFactory(column -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button editButton = new Button("Edit");
            private final Button recordButton = new Button("Entry/Exit");
            private final Button historyButton = new Button("History");

            {
                // Initialize buttons
                editButton.setStyle("""
                    -fx-background-color: #f39c12;
                    -fx-text-fill: white;
                    -fx-cursor: hand;
                    -fx-font-size: 11px;
                    -fx-padding: 3 8;
                    """);

                recordButton.setStyle("""
                    -fx-background-color: #3498db;
                    -fx-text-fill: white;
                    -fx-cursor: hand;
                    -fx-font-size: 11px;
                    -fx-padding: 3 8;
                    """);

                historyButton.setStyle("""
                    -fx-background-color: #2ecc71;
                    -fx-text-fill: white;
                    -fx-cursor: hand;
                    -fx-font-size: 11px;
                    -fx-padding: 3 8;
                    """);

                buttons.setAlignment(Pos.CENTER);
                buttons.getChildren().addAll(editButton, recordButton, historyButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Traveler traveler = getTableView().getItems().get(getIndex());
                    
                    editButton.setOnAction(event -> {
                        TravelerEditView.show(stage, traveler);
                        loadTravelers(tableView);
                    });

                    recordButton.setOnAction(event -> {
                        EntryExitRecordView.show(stage, traveler);
                    });

                    historyButton.setOnAction(event -> {
                        // TODO: Implement history view
                        showMessage("Coming Soon", "Travel history view will be implemented soon!");
                    });

                    setGraphic(buttons);
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
        searchBox.setPadding(new Insets(10));
        searchBox.getChildren().addAll(searchField, searchButton, refreshButton);
        
        // Add status label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: #7f8c8d;");
        
        // Search functionality
        searchButton.setOnAction(e -> {
            try {
                TravelerDAO dao = new TravelerDAO();
                String searchTerm = searchField.getText().trim();
                if (!searchTerm.isEmpty()) {
                    List<Traveler> results = dao.searchTravelers(searchTerm);
                    tableView.setItems(FXCollections.observableArrayList(results));
                    statusLabel.setText("Found " + results.size() + " travelers");
                } else {
                    loadTravelers(tableView);
                }
            } catch (SQLException ex) {
                showError("Search Error", "Could not perform search", ex.getMessage());
            }
        });
        
        // Add enter key handler for search
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER) {
                searchButton.fire();
            }
        });
        
        // Refresh functionality
        refreshButton.setOnAction(e -> {
            searchField.clear();
            loadTravelers(tableView);
        });
        
        // Create layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(searchBox, tableView, statusLabel);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        
        // Create scene
        Scene scene = new Scene(layout, 900, 600);
        stage.setScene(scene);
        
        // Initial load
        loadTravelers(tableView);
        
        stage.show();
    }
    
    private static void loadTravelers(TableView<Traveler> tableView) {
        try {
            TravelerDAO dao = new TravelerDAO();
            List<Traveler> travelers = dao.findAll();
            tableView.setItems(FXCollections.observableArrayList(travelers));
            
            // Update status
            tableView.getScene().getRoot()
                    .lookupAll(".label")
                    .stream()
                    .filter(node -> node instanceof Label)
                    .map(node -> (Label) node)
                    .filter(label -> label.getText() != null && label.getText().contains("Found"))
                    .findFirst()
                    .ifPresent(label -> label.setText("Showing " + travelers.size() + " travelers"));
                    
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
    
    private static void showMessage(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}