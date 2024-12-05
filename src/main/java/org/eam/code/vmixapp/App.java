package org.eam.code.vmixapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.eam.code.vmixapp.util.DBInitializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Sequences.fxml")));
        Scene scene = new Scene(parent);
        stage.setTitle("VMix App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DBInitializer.initializeDatabase();
        launch();

    }

}
