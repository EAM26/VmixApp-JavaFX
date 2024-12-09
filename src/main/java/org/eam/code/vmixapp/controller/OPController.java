package org.eam.code.vmixapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Camera;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.eam.code.vmixapp.App;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.service.MyCameraService;
import org.eam.code.vmixapp.util.Alarm;
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
    private Button btnDeleteCam;

    @FXML
    private Button btnSaveCam;

    @FXML
    private Button btnUpdateCam;

    @FXML
    private Button btnClearCam;

    @FXML
    private TableColumn<Camera, String> colName;

    @FXML
    private TableColumn<Camera, Integer> colRef;

    @FXML
    private TableView<MyCamera> tableCams;

    @FXML
    private TextField tfRef;

    @FXML
    private TextField tfNameCam;

    @FXML
    private Button btnClearSceneFields;


    @FXML
    private Button btnDeleteScene;


    @FXML
    private Button btnSaveScene;


    @FXML
    private Button btnUpdateScene;


    @FXML
    private TableView<?> tableScenes;

    @FXML
    private TextField tfDescription;



    @FXML
    private TextField tfNameScene;

    @FXML
    private TextField tfNumScene;





    @FXML
    void clearFieldsSence(ActionEvent event) {

    }




    @FXML
    void deleteScene(ActionEvent event) {

    }


    @FXML
    void saveScene(ActionEvent event) {

    }



    @FXML
    void updateScene(ActionEvent event) {

    }

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
    void getSelectedData(MouseEvent event) {
        MyCamera selectedCamera = tableCams.getSelectionModel().getSelectedItem();
        if (selectedCamera != null) {
            tfRef.setText(String.valueOf(selectedCamera.getRef()));
            tfNameCam.setText(selectedCamera.getName());
            btnSaveCam.setDisable(true);
        }
    }

    @FXML
    void clearFieldsCam(ActionEvent event) {
        clear();
    }

    @FXML
    void createCam(ActionEvent event) {
        if (validateTextFields()) {
            try {
                myCameraService.createCam(tfRef.getText(), tfNameCam.getText(), SelectedSequence.getSelectedSequence());
                showCameras();
                clear();
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                Alarm.showError("Error in creating new Camera.");
            }
        }
    }

    @FXML
    void deleteCam(ActionEvent event) {
        MyCamera selectedCam = tableCams.getSelectionModel().getSelectedItem();
        if (selectedCam != null) {
            if (Alarm.showAskConfirmation(selectedCam.getRef(), selectedCam.getName())) {
                try {
                    myCameraService.deleteCam(selectedCam.getId());
                    showCameras();
                    clear();
                } catch (RuntimeException e) {
                    System.err.println(e.getMessage());
                    Alarm.showError("Error in deleting camera.");
                }
            }
        }

    }


    @FXML
    void updateCam(ActionEvent event) {
        if (validateTextFields()) {
            MyCamera selectedCam = tableCams.getSelectionModel().getSelectedItem();
            try {
                if (selectedCam != null) {
                    myCameraService.updateCam(tfRef.getText(), tfNameCam.getText(), selectedCam.getId());
                    showCameras();
                    clear();
                }
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                Alarm.showError("Error in updating camera.");
            }
        }

    }


    @FXML
    void switchToMain(ActionEvent event) {
        try {
            App.switchToMainScreen();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            Alarm.showError("Error in switching to Main Screen.");
        }
    }

    private void clear() {
        tfRef.setText("");
        tfNameCam.setText("");
        btnSaveCam.setDisable(false);
    }

    private boolean validateTextFields() {
        return !tfRef.getText().isBlank() && !tfNameCam.getText().isBlank();
    }
}
