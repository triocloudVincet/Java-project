/*
 * Border Control System
 * Copyright (c) 2024 David Nyabuto. All rights reserved.
 * 
 * This system manages border control operations including:
 * - Traveler registration and management
 * - Visa and permit processing
 * - Entry/Exit recording
 * - Security flag management
 * - Reporting and analytics
 *
 * Version: 1.0
 * Author: David Nyabuto
 * Date: November 2024
 */

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
 import javafx.animation.FadeTransition;
 import java.time.LocalDateTime;
 import java.time.format.DateTimeFormatter;
 import org.kordamp.ikonli.javafx.FontIcon;
 import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
 import javafx.scene.effect.DropShadow;
 import javafx.stage.StageStyle;
 import javafx.application.Platform;
 import javafx.scene.Node;
 import java.util.concurrent.atomic.AtomicInteger;
 import com.bordercontrol.views.*;
 
 /**
  * Main application class for the Border Control System.
  * Implements a modern dashboard interface with real-time updates
  * and professional user experience.
  */
 public class Main extends Application {
     private Stage primaryStage;
     private BorderPane mainLayout;
     private VBox contentArea;
     private Label statusLabel;
     private VBox notificationPane;
     private AtomicInteger notificationCount = new AtomicInteger(0);
     private static final String VERSION = "1.0";
     
     @Override
     public void start(Stage primaryStage) {
         this.primaryStage = primaryStage;
         primaryStage.initStyle(StageStyle.UNDECORATED);
         createDashboard();
         showSplashScreen();
     }
 
     private void showSplashScreen() {
         Stage splash = new Stage(StageStyle.UNDECORATED);
         VBox splashLayout = new VBox(20);
         splashLayout.setAlignment(Pos.CENTER);
         splashLayout.setStyle("""
             -fx-background-color: #2c3e50;
             -fx-padding: 20;
             """);
 
         Label title = new Label("Border Control System");
         title.setFont(Font.font("System", FontWeight.BOLD, 28));
         title.setStyle("-fx-text-fill: white;");
 
         Label version = new Label("Version " + VERSION);
         version.setStyle("-fx-text-fill: #bdc3c7;");
 
         Label copyright = new Label("© 2024 David Nyabuto");
         copyright.setStyle("-fx-text-fill: #bdc3c7;");
 
         ProgressBar progress = new ProgressBar();
         progress.setPrefWidth(200);
 
         splashLayout.getChildren().addAll(title, version, progress, copyright);
 
         Scene splashScene = new Scene(splashLayout, 400, 300);
         splash.setScene(splashScene);
         splash.show();
 
         // Simulate loading
         Timeline timeline = new Timeline(
             new KeyFrame(Duration.ZERO, evt -> progress.setProgress(0)),
             new KeyFrame(Duration.seconds(2), evt -> {
                 progress.setProgress(1);
                 splash.close();
                 primaryStage.show();
             })
         );
         timeline.play();
     }
 
     private void createDashboard() {
         mainLayout = new BorderPane();
         
         // Create components
         VBox sidebar = createSidebar();
         HBox header = createHeader();
         HBox statusBar = createStatusBar();
         
         // Create notification pane
         notificationPane = new VBox(10);
         notificationPane.setStyle("""
             -fx-background-color: white;
             -fx-padding: 10;
             -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);
             """);
         notificationPane.setVisible(false);
         
         // Create content area with dashboard
         contentArea = new VBox(20);
         contentArea.setPadding(new Insets(20));
         contentArea.setStyle("-fx-background-color: #f4f4f4;");
         
         // Add dashboard components
         contentArea.getChildren().addAll(
             createDashboardOverview(),
             createQuickActions(),
             createRecentActivity()
         );
         
         // Create scrollable content
         ScrollPane scrollPane = new ScrollPane(contentArea);
         scrollPane.setFitToWidth(true);
         scrollPane.setStyle("-fx-background-color: transparent;");
         
         // Stack notification pane over content
         StackPane centerStack = new StackPane(scrollPane, notificationPane);
         StackPane.setAlignment(notificationPane, Pos.TOP_RIGHT);
         StackPane.setMargin(notificationPane, new Insets(10, 10, 0, 0));
         
         // Set layout
         mainLayout.setTop(header);
         mainLayout.setLeft(sidebar);
         mainLayout.setCenter(centerStack);
         mainLayout.setBottom(statusBar);
         
         // Create scene with maximized window
         Scene scene = new Scene(mainLayout);
         primaryStage.setTitle("Border Control System");
         primaryStage.setMaximized(true);
         primaryStage.setScene(scene);
     }