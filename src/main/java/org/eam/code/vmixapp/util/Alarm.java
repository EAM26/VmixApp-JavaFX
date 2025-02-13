package org.eam.code.vmixapp.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alarm {

    public static boolean confirmationDelete(int id, String name) {
        return confirmationDelete(String.valueOf(id), name);
    }

    public static boolean confirmationDelete(String ref, String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete: " + ref + " - " + name);
        alert.setContentText("This action cannot be undone.");
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    public static boolean confirmationDelete(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete: " +  name);
        alert.setContentText("This action cannot be undone.");
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    public static boolean confirmationInsert(int sceneNumber, String sceneName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Insert Scene");
        alert.setHeaderText("A scene with this number already exists.\n" +
                "Proceed to insert scene as number: " + sceneNumber + ", with name: " + sceneName + "?");
        alert.setContentText("This action cannot be undone.");
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }



    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
