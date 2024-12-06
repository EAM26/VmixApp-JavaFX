package org.eam.code.vmixapp.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Camera;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.eam.code.vmixapp.App;
import org.eam.code.vmixapp.dao.CameraDAO;
import org.eam.code.vmixapp.service.CameraService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OPController implements Initializable {
    private final CameraService cameraService;

    public OPController() {
        this.cameraService = new CameraService(new CameraDAO());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCameras();
    }

    @FXML
    private Button btnSwitch;

    @FXML
    private TableColumn<Camera, String> colName;

    @FXML
    private TableColumn<Camera, Integer> colNumber;

    @FXML
    private TableView<org.eam.code.vmixapp.model.Camera> tableCameras;

    private void showCameras() {
        ObservableList<org.eam.code.vmixapp.model.Camera> cameraList = cameraService.getCameras();
        try {
            tableCameras.setItems(cameraList);
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void switchToMain(ActionEvent event) {
        try {
            App.switchToMainScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
