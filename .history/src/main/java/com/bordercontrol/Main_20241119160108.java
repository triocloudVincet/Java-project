package com.bordercontrol;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Animation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import com.bordercontrol.views.*;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane mainLayout;
    private VBox contentArea;
    private Label statusLabel;
    
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
        
        // Create header
        HBox header = createHeader();
        mainLayout.setTop(header);
        
        // Create content area with dashboard
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));
        contentArea.setStyle("-fx-background-color: #f4f4f4;");
        
        // Add dashboard components
        contentArea.getChildren().addAll(
            createDashboardOverview(),
            createRecentActivity()
        );
        
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        mainLayout.setCenter(scrollPane);
        
        // Create status bar
        HBox statusBar = createStatusBar();
        mainLayout.setBottom(statusBar);
        
        // Create scene with maximized window
        Scene scene = new Scene(mainLayout);
        primaryStage.setTitle("Border Control System");
        primaryStage.setMaximized(true);
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

        // Menu Items with icons
        MenuItem[] menuItems = {
            new MenuItem("Register New Traveler", FontAwesomeSolid.USER_PLUS),
            new MenuItem("View Travelers", FontAwesomeSolid.USERS),
            new MenuItem("Check Eligibility", FontAwesomeSolid.CHECK_CIRCLE),
            new MenuItem("Issue Visa/Permit", FontAwesomeSolid.PASSPORT),
            new MenuItem("Flag Management", FontAwesomeSolid.FLAG),
            new MenuItem("Entry/Exit Records", FontAwesomeSolid.EXCHANGE_ALT),
            new MenuItem("Reports", FontAwesomeSolid.CHART_BAR),
            new MenuItem("Settings", FontAwesomeSolid.COG)
        };

        sidebar.getChildren().add(titleLabel);
        sidebar.getChildren().add(new Separator());

        for (MenuItem item : menuItems) {
            Button menuButton = createMenuButton(item);
            sidebar.getChildren().add(menuButton);
        }

        // Logout at bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button logoutButton = createMenuButton(new MenuItem("Exit", FontAwesomeSolid.SIGN_OUT_ALT));
        logoutButton.setStyle("""
            -fx-background-color: #c0392b;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-font-size: 14px;
            -fx-cursor: hand;
            """);

        sidebar.getChildren().addAll(spacer, new Separator(), logoutButton);
        
        return sidebar;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setStyle("""
            -fx-background-color: white;
            -fx-padding: 10 20;
            -fx-spacing: 20;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);
            """);

        // Current Date/Time
        Label dateTimeLabel = new Label();
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            dateTimeLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        
        // User info
        Label userLabel = new Label("Admin User");
        userLabel.setStyle("-fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        header.getChildren().addAll(dateTimeLabel, spacer, userLabel);
        return header;
    }

    private HBox createDashboardOverview() {
        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
            createStatCard("Total Travelers", "1,234", FontAwesomeSolid.USERS, "#2ecc71"),
            createStatCard("Active Flags", "23", FontAwesomeSolid.FLAG, "#e74c3c"),
            createStatCard("Entries Today", "156", FontAwesomeSolid.PLANE_ARRIVAL, "#3498db"),
            createStatCard("Pending Visas", "45", FontAwesomeSolid.PASSPORT, "#f1c40f")
        );
        return statsRow;
    }

    private VBox createStatCard(String title, String value, String icon, String color) {
        VBox card = new VBox(10);
        card.setStyle(String.format("""
            -fx-background-color: white;
            -fx-padding: 20;
            -fx-background-radius: 5;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);
            -fx-border-color: %s;
            -fx-border-width: 0 0 0 5;
            -fx-border-radius: 5;
            """, color));
        card.setPrefWidth(200);

        FontIcon iconView = new FontIcon(icon);
        iconView.setIconSize(24);
        iconView.setIconColor(Color.web(color));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #7f8c8d;");
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("""
            -fx-font-size: 24;
            -fx-font-weight: bold;
            """);

        card.getChildren().addAll(iconView, titleLabel, valueLabel);
        return card;
    }

    private VBox createRecentActivity() {
        VBox activity = new VBox(10);
        activity.setStyle("""
            -fx-background-color: white;
            -fx-padding: 20;
            -fx-background-radius: 5;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);
            """);

        Label titleLabel = new Label("Recent Activity");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        ListView<String> activityList = new ListView<>();
        activityList.getItems().addAll(
            "John Doe entered through JKIA",
            "New flag raised for passport #ABC123",
            "Visa issued to Jane Smith",
            "Exit recorded for James Brown"
        );
        activityList.setPrefHeight(200);

        activity.getChildren().addAll(titleLabel, activityList);
        return activity;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox(10);
        statusBar.setStyle("""
            -fx-background-color: #f8f9fa;
            -fx-padding: 5 10;
            -fx-border-color: #dee2e6;
            -fx-border-width: 1 0 0 0;
            """);

        statusLabel = new Label("System Status: Online");
        statusLabel.setStyle("-fx-text-fill: #2ecc71;");
        statusBar.getChildren().add(statusLabel);
        
        return statusBar;
    }

    private Button createMenuButton(MenuItem item) {
        Button button = new Button(item.text);
        
        // Create icon
        FontIcon icon = new FontIcon(item.icon);
        icon.setIconColor(Color.WHITE);
        icon.setIconSize(16);
        
        button.setGraphic(icon);
        button.setGraphicTextGap(10);
        
        button.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-font-size: 14px;
            -fx-cursor: hand;
            -fx-alignment: CENTER_LEFT;
            -fx-min-width: 230;
            """);
        
        button.setOnMouseEntered(e -> 
            button.setStyle("""
                -fx-background-color: #34495e;
                -fx-text-fill: white;
                -fx-padding: 10 20;
                -fx-font-size: 14px;
                -fx-cursor: hand;
                -fx-alignment: CENTER_LEFT;
                -fx-min-width: 230;
                """)
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: white;
                -fx-padding: 10 20;
                -fx-font-size: 14px;
                -fx-cursor: hand;
                -fx-alignment: CENTER_LEFT;
                -fx-min-width: 230;
                """)
        );

        button.setOnAction(e -> handleMenuClick(item.text));
        
        return button;
    }

    private record MenuItem(String text, String icon) {}

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
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
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