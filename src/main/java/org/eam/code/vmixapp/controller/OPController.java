package org.eam.code.vmixapp.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Camera;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.eam.code.vmixapp.App;
import org.eam.code.vmixapp.dao.CameraDAO;
import org.eam.code.vmixapp.service.CameraService;
import org.eam.code.vmixapp.util.SelectedSequence;

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
        setLabel();
        showCameras();
    }

    @FXML
    private Label lbSequence;

    @FXML
    private Button btnSwitch;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button clear;

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

    void setLabel() {
        lbSequence.setText("SEQUENCE: " + SelectedSequence.getSelectedSequence().getName());
    }

    @FXML
    void clearFields(ActionEvent event) {

    }

    @FXML
    void createCam(ActionEvent event) {

    }

    @FXML
    void deleteCam(ActionEvent event) {

    }

    @FXML
    void updateCam(ActionEvent event) {

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
