package com.polytech.arkanoid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Arkanoid");
        Scene scene = new Scene(root,Color.rgb(50,50,50));
        String image = getClass().getResource("icon.jpg").toExternalForm();
        Image icon = new Image(image);
        primaryStage.getIcons().add(icon);
        String url = getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(url);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}