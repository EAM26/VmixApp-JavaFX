package org.eam.code.vmixapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.eam.code.vmixapp.App;

import java.io.IOException;

public class OPController {

    @FXML
    private Button btnSwitch;

    @FXML
    void switchToMain(ActionEvent event) throws IOException {
        App.switchToMainScreen();
    }

}
