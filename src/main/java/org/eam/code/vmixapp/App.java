package org.eam.code.vmixapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.eam.code.vmixapp.util.DBInitializer;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        switchToMainScreen();
        stage.show();
    }

    public static void switchToMainScreen() throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("/fxml/Sequences.fxml")));
        Scene scene = new Scene(parent);
        primaryStage.setTitle("VMix App");
        primaryStage.setScene(scene);
    }

    public static void switchToOPScreen() throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("/fxml/OPScreen.fxml")));
        Scene scene = new Scene(parent);
        primaryStage.setTitle("OPScreen");
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();

    }

}
