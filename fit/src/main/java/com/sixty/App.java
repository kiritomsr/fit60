package com.sixty;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class App extends Application{

    private static App app = new App();
    private AnchorPane anchorPane;
    private Stage primaryStage;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException {
        this.app.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.setResizable(false);
        primaryStage.setScene(initialize());

        jump("login");
        primaryStage.show();
    }

    public static void jump(String url) throws IOException{

        app.primaryStage.setTitle(""+url+"");
        app.anchorPane.getChildren().clear();
        Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getClassLoader().getResource("fxml/" + url + ".fxml")));
        app.anchorPane.getChildren().add(root);

    }

    public static void show(){
        app.primaryStage.show();
    }

    public Scene initialize() {
        Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        Scene root = new Scene(pane);
        StackPane stackPane = new StackPane();
        root.setFill(Color.TRANSPARENT);
        stackPane.setOpacity(1);
        stackPane.setPrefHeight(700);
        stackPane.setPrefWidth(1066);
        stackPane.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #808080;");
        ImageView imageView = new ImageView(new Image("file:src/main/resources/picture/icon/close.png"));

        imageView.setFitHeight(23);
        imageView.setFitWidth(23);
        imageView.setLayoutX(1024);
        imageView.setLayoutY(9);

        EventHandler<MouseEvent> closeClick = e -> {
            app.primaryStage.close();
            Platform.exit();
        };
        imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, closeClick);
        app.anchorPane = new AnchorPane();
        app.anchorPane.setLayoutX(0);
        app.anchorPane.setLayoutY(50);
        app.anchorPane.setPrefHeight(550);
        app.anchorPane.setPrefWidth(1066);
        app.anchorPane.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        app.anchorPane.setOpacity(1);
        pane.getChildren().addAll(stackPane, imageView, app.anchorPane);

        AtomicReference<Double> xOffSet = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffSet = new AtomicReference<>((double) 0);
        root.setOnMousePressed(event -> {
            xOffSet.set(event.getSceneX());
            yOffSet.set(event.getSceneY());
        });
        root.setOnMouseDragged(event -> {
            app.primaryStage.setX(event.getScreenX() - xOffSet.get());
            app.primaryStage.setY(event.getScreenY() - yOffSet.get());
        });
        return root;
    }

}

