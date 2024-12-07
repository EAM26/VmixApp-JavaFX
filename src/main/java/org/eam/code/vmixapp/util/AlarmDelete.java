package org.eam.code.vmixapp.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlarmDelete {

    public static boolean show(int id, String name) {
        return show(String.valueOf(id), name);
    }
    public static boolean show(String ref, String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete: " + ref + " - " + name);
        alert.setContentText("This action cannot be undone.");
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}
