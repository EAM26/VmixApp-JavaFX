package org.eam.code.vmixapp;

import javafx.application.Application;
import org.eam.code.vmixapp.util.DBInitializer;

public class Launcher {
    public static void main(String[] args) {
        DBInitializer.initializeDatabase();
        Application.launch(App.class, args);
    }
}
