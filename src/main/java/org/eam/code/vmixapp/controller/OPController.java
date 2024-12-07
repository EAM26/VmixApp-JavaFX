package org.eam.code.vmixapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Camera;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.eam.code.vmixapp.App;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.service.MyCameraService;
import org.eam.code.vmixapp.util.SelectedSequence;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class OPController implements Initializable {
    private final MyCameraService myCameraService;

    public OPController() {
        this.myCameraService = new MyCameraService(new MyCameraDAO());
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
    private TableColumn<Camera, Integer> colRef;

    @FXML
    private TableView<MyCamera> tableCams;

    @FXML
    private TextField tfRef;

    @FXML
    private TextField tfName;

    private void showCameras() {
        ObservableList<MyCamera> myCameraList = myCameraService.getCameras();
        FXCollections.sort(myCameraList, Comparator.comparing(MyCamera::getRef));
        try {
            tableCams.setItems(myCameraList);
            colRef.setCellValueFactory(new PropertyValueFactory<>("Ref"));
            colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void setLabel() {
        lbSequence.setText("SEQUENCE: " + SelectedSequence.getSelectedSequence().getName());
    }

    @FXML
    void getSelectedData() {
        MyCamera selectedCamera = tableCams.getSelectionModel().getSelectedItem();
        if(selectedCamera != null) {
            tfRef.setText(String.valueOf(selectedCamera.getRef()));
            tfName.setText(selectedCamera.getName());
            btnSave.setDisable(true);
        }

    }

    @FXML
    void clearFields(ActionEvent event) {
        clear();
    }

    @FXML
    void createCam(ActionEvent event) {
        try {
            myCameraService.createCam(tfRef.getText(), tfName.getText(), SelectedSequence.getSelectedSequence());
            showCameras();
            clear();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void deleteCam(ActionEvent event) {

    }

    @FXML
    void updateCam(ActionEvent event) {
        MyCamera selectedCam = tableCams.getSelectionModel().getSelectedItem();
        try {
            if(selectedCam != null) {
                myCameraService.updateCam(tfRef.getText(), tfName.getText(), selectedCam.getId());
                showCameras();
                clear();
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
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

    private void clear() {
        tfRef.setText("");
        tfName.setText("");
        btnSave.setDisable(false);

    }



}
