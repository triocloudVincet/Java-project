package com.bordercontrol;

import com.bordercontrol.views.GenerateReportView;
import com.bordercontrol.views.SettingsView;
import com.bordercontrol.views.TravelerListView;
import com.bordercontrol.views.TravelerRegistrationView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane mainLayout;
    private VBox contentArea;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createDashboard();
    }

    private void createDashboard() {
        mainLayout = new BorderPane();
        
        // Create sidebar
        VBox sidebar = createSidebar();
        mainLayout.setLeft(sidebar);
        
        // Create content area
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));
        contentArea.setStyle("-fx-background-color: #f4f4f4;");
        mainLayout.setCenter(contentArea);
        
        // Welcome screen
        showWelcomeScreen();
        
        // Create scene with maximized window
        Scene scene = new Scene(mainLayout);
        primaryStage.setTitle("Border Control System");
        primaryStage.setMaximized(true); // Start maximized
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("""
            -fx-background-color: #2c3e50;
            -fx-padding: 10;
            """);

        // System Title
        Label titleLabel = new Label("Border Control");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setStyle("""
            -fx-text-fill: white;
            -fx-padding: 20;
            -fx-alignment: center;
            """);

        // Menu Items
        String[] menuItems = {
            "Register New Traveler",
            "View Travelers",
            "Edit Traveler",
            "Check Eligibility",
            "Issue Visa/Permit",
            "Flag Management",
            "Entry/Exit Records",
            "Reports",
            "Settings"
        };

        sidebar.getChildren().add(titleLabel);
        sidebar.getChildren().add(new Separator());

        for (String menuItem : menuItems) {
            Button menuButton = createMenuButton(menuItem);
            sidebar.getChildren().add(menuButton);
        }

        // Logout at bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button logoutButton = createMenuButton("Exit");
        logoutButton.setStyle("""
            -fx-background-color: #c0392b;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-font-size: 14px;
            -fx-cursor: hand;
            -fx-min-width: 230;
            -fx-alignment: CENTER_LEFT;
            """);

        sidebar.getChildren().addAll(spacer, new Separator(), logoutButton);
        
        return sidebar;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: #ecf0f1;
            -fx-padding: 10 20;
            -fx-font-size: 14px;
            -fx-cursor: hand;
            -fx-min-width: 230;
            -fx-alignment: CENTER_LEFT;
            """);
        
        button.setOnMouseEntered(e -> 
            button.setStyle("""
                -fx-background-color: #34495e;
                -fx-text-fill: white;
                -fx-padding: 10 20;
                -fx-font-size: 14px;
                -fx-cursor: hand;
                -fx-min-width: 230;
                -fx-alignment: CENTER_LEFT;
                """)
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: #ecf0f1;
                -fx-padding: 10 20;
                -fx-font-size: 14px;
                -fx-cursor: hand;
                -fx-min-width: 230;
                -fx-alignment: CENTER_LEFT;
                """)
        );

        button.setOnAction(e -> handleMenuClick(text));
        
        return button;
    }

    private void handleMenuClick(String menuItem) {
        clearContent();
        
        switch (menuItem) {
            case "Register New Traveler":
                TravelerRegistrationView.show(primaryStage);
                break;
            case "View Travelers":
                TravelerListView.show(primaryStage);
                break;
            case "Check Eligibility":
                showMessage("Select a traveler from View Travelers to check eligibility");
                break;
            case "Issue Visa/Permit":
                showMessage("Select a traveler from View Travelers to issue visa");
                break;
            case "Flag Management":
                showMessage("Select a traveler from View Travelers to manage flags");
                break;
            case "Entry/Exit Records":
                showMessage("Select a traveler from View Travelers to manage records");
                break;
            case "Reports":
                GenerateReportView.show(primaryStage);
                break;
            case "Settings":
                SettingsView.show(primaryStage);
                break;
            case "Exit":
                primaryStage.close();
                break;
        }
    }

    private void clearContent() {
        contentArea.getChildren().clear();
    }

    private void showWelcomeScreen() {
        clearContent();
        
        Label welcomeLabel = new Label("Welcome to Border Control System");
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        welcomeLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label infoLabel = new Label("Select an option from the menu to get started");
        infoLabel.setFont(Font.font("System", 16));
        infoLabel.setStyle("-fx-text-fill: #7f8c8d;");
        
        contentArea.setAlignment(Pos.CENTER);
        contentArea.getChildren().addAll(welcomeLabel, infoLabel);
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}