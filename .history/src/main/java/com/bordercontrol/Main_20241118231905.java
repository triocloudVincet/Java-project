package com.bordercontrol;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            VBox root = new VBox(10); // 10 is spacing between elements
            root.setStyle("-fx-padding: 20;"); // Add padding around the edges
            
            Label label = new Label("Border Control System");
            label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
            
            root.getChildren().add(label);
            
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Border Control System");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}