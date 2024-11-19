package com.bordercontrol.views;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.bordercontrol.dao.EntryExitDAO;
import com.bordercontrol.dao.FlaggedTravelerDAO;
import com.bordercontrol.dao.TravelerDAO;
import com.bordercontrol.models.FlaggedTraveler;
import com.bordercontrol.models.Traveler;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TravelerListView {
    private static TravelerDAO travelerDao = new TravelerDAO();
    private static EntryExitDAO entryExitDao = new EntryExitDAO();
    private static FlaggedTravelerDAO flaggedTravelerDao = new FlaggedTravelerDAO();
    
    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("View Travelers");
        
        // Create TableView
        TableView<Traveler> tableView = new TableView<>();

        // Set row factory for flag indicators
        tableView.setRowFactory(tv -> new TableRow<Traveler>() {
            @Override
            protected void updateItem(Traveler traveler, boolean empty) {
                super.updateItem(traveler, empty);
                if (traveler == null || empty) {
                    setStyle("");
                    setTooltip(null);
                } else if (traveler.isFlagged()) {
                    // Light red background for flagged travelers
                    setStyle("-fx-background-color: #ffebee;");
                    
                    // Add tooltip with flag information
                    Tooltip tooltip = new Tooltip(
                        "Flagged: " + traveler.getActiveFlagType()
                    );
                    setTooltip(tooltip);
                } else {
                    setStyle("");
                    setTooltip(null);
                }
            }
        });
        
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
        actionCol.setPrefWidth(300);
        actionCol.setCellFactory(column -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button editButton = new Button("Edit");
            private final Button recordButton = new Button("Entry/Exit");
            private final Button historyButton = new Button("History");
            private final Button flagButton = new Button("Flag");
            private final Button viewFlagsButton = new Button("View Flags");

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

                flagButton.setStyle("""
                    -fx-background-color: #e74c3c;
                    -fx-text-fill: white;
                    -fx-cursor: hand;
                    -fx-font-size: 11px;
                    -fx-padding: 3 8;
                    """);

                viewFlagsButton.setStyle("""
                    -fx-background-color: #9b59b6;
                    -fx-text-fill: white;
                    -fx-cursor: hand;
                    -fx-font-size: 11px;
                    -fx-padding: 3 8;
                    """);

                buttons.setAlignment(Pos.CENTER);
                buttons.getChildren().addAll(editButton, recordButton, historyButton, flagButton, viewFlagsButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Traveler traveler = getTableView().getItems().get(getIndex());
                    
                    // Update flag button based on status
                    if (traveler.isFlagged()) {
                        flagButton.setText("Resolve");
                        flagButton.setStyle("""
                            -fx-background-color: #8e44ad;
                            -fx-text-fill: white;
                            -fx-cursor: hand;
                            -fx-font-size: 11px;
                            -fx-padding: 3 8;
                            """);
                    } else {
                        flagButton.setText("Flag");
                        flagButton.setStyle("""
                            -fx-background-color: #e74c3c;
                            -fx-text-fill: white;
                            -fx-cursor: hand;
                            -fx-font-size: 11px;
                            -fx-padding: 3 8;
                            """);
                    }
                    
                    editButton.setOnAction(event -> {
                        TravelerEditView.show(stage, traveler);
                        loadTravelers(tableView);
                    });

                    recordButton.setOnAction(event -> {
                        EntryExitRecordView.show(stage, traveler);
                    });

                    historyButton.setOnAction(event -> {
                        TravelerHistoryView.show(stage, traveler);
                    });

                    flagButton.setOnAction(event -> {
                        try {
                            if (traveler.isFlagged()) {
                                FlaggedTraveler activeFlag = flaggedTravelerDao.getActiveFlag(traveler.getId());
                                if (activeFlag != null) {
                                    FlagResolutionView.show(stage, traveler, activeFlag);
                                }
                            } else {
                                FlagTravelerView.show(stage, traveler);
                            }
                            loadTravelers(tableView); // Refresh list
                        } catch (SQLException ex) {
                            showError("Error", "Could not process flag operation", ex.getMessage());
                        }
                    });

                    viewFlagsButton.setOnAction(event -> {
                        TravelerFlagHistoryView.show(stage, traveler);
                    });

                    setGraphic(buttons);
                }
            }
        });
        
        // Add columns to table
        tableView.getColumns().addAll(passportCol, nameCol, nationalityCol, dobCol, expiryCol, actionCol);
        
        // Create search box with flag filter
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(10));

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search by passport number or name...");
        searchField.setPrefWidth(250);

        // Flag filter
        ComboBox<String> flagFilter = new ComboBox<>();
        flagFilter.getItems().addAll("All Travelers", "Flagged Only", "Not Flagged");
        flagFilter.setValue("All Travelers");
        flagFilter.setStyle("-fx-pref-width: 150px;");
        
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
        
        searchBox.getChildren().addAll(searchField, flagFilter, searchButton, refreshButton);
        
        // Add status label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: #7f8c8d;");
        
        // Search functionality with flag filter
        searchButton.setOnAction(e -> {
            try {
                String searchTerm = searchField.getText().trim();
                List<Traveler> results = travelerDao.searchTravelers(searchTerm);

                // Apply flag filter
                if (flagFilter.getValue().equals("Flagged Only")) {
                    results.removeIf(t -> !t.isFlagged());
                } else if (flagFilter.getValue().equals("Not Flagged")) {
                    results.removeIf(t -> t.isFlagged());
                }

                tableView.setItems(FXCollections.observableArrayList(results));
                statusLabel.setText("Found " + results.size() + " travelers");
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
            flagFilter.setValue("All Travelers");
            loadTravelers(tableView);
        });
        
        // Create layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(searchBox, tableView, statusLabel);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        
        // Create scene
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        
        // Initial load
        loadTravelers(tableView);
        
        stage.show();
    }
    
    private static void loadTravelers(TableView<Traveler> tableView) {
        try {
            List<Traveler> travelers = travelerDao.findAll();
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
}