package org.eam.code.vmixapp.controller;

import javafx.beans.property.SimpleStringProperty;
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
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Recorder;
import org.eam.code.vmixapp.model.Scene;
import org.eam.code.vmixapp.service.CameraImporter;
import org.eam.code.vmixapp.service.MyCameraService;
import org.eam.code.vmixapp.service.SceneService;
import org.eam.code.vmixapp.util.Alarm;
import org.eam.code.vmixapp.util.SelectedSequence;
import org.eam.code.vmixapp.util.VMRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class OPController implements Initializable {

    private final MyCameraService myCameraService;
    private final SceneService sceneService;
    private final Recorder recorder;
    private final VMRequest request;
    private final CameraImporter cameraImporter;
    private static final Logger logger = LoggerFactory.getLogger(OPController.class);


    public ObservableList<Scene> sceneList;

    public OPController() {
        this.cameraImporter = new CameraImporter();
        this.myCameraService = new MyCameraService();
        this.sceneService = new SceneService();
        this.recorder = new Recorder();
        this.request = new VMRequest();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLabel();
        showCameras();
        showScenes();
    }

    @FXML
    private Label lbSequence;

    @FXML
    private Button btnSwitch;

    //    Recorder elements
    @FXML
    private Button btnCut;

    @FXML
    private Button btnSetPreview;

    @FXML
    private Button btnClearPreview;

    @FXML
    private TextField tfActual;

    @FXML
    private TextField tfPreview;

    @FXML
    void cutScene(ActionEvent event) {
        if (recorder.getPreview() != null) {
            int indexPrev = sceneList.indexOf(recorder.getPreview());
            if (indexPrev == -1) {
//                Find the old preview scene in the recreated sceneList.
                for (Scene scene : sceneList) {
                    if (scene.getId() == recorder.getPreview().getId()) {
                        indexPrev = sceneList.indexOf(scene);
                    }
                }
            }
            try {
                request.cut();
                recorder.setActual(recorder.getPreview());
                if (indexPrev < sceneList.size() - 1) {
                    request.setPreview(sceneList.get(indexPrev + 1));
                    recorder.setPreview(sceneList.get(indexPrev + 1));
                } else {
                    recorder.setPreview(null);
                }
            } catch (RuntimeException e) {
                logger.error("Error caught in cutScene" + e.getMessage(), e);
                Alarm.showError("Error in cutting to new scene.");
            }
        }
        showRecorderFields();
    }

    @FXML
    void clearPreviewWithButton(ActionEvent event) {
        recorder.setPreview(null);
        showRecorderFields();
    }

    @FXML
    boolean setPreviewWithButton(ActionEvent event) {
        if (!validateSceneList()) {
            return false;
        }
        Scene sceneToSetPreview;
        if (getSelectedSceneData() == null) {
            sceneToSetPreview = sceneList.getFirst();
        } else {
            sceneToSetPreview = getSelectedSceneData();
        }
        try {
            request.setPreview(sceneToSetPreview);
            recorder.setPreview(sceneToSetPreview);
        } catch (RuntimeException e) {
            logger.error("Error caught in setPreviewWithButton" + e.getMessage(), e);
            Alarm.showError("Error in setting preview to VMix.");
        }

        showRecorderFields();
        tableScenes.getSelectionModel().clearSelection();
        return true;
    }


    private void showRecorderFields() {
        Map<Scene, String> sceneColorMap = new HashMap<>();
        if (recorder.getPreview() != null) {
            tfPreview.setText("Scene " + recorder.getPreview().getNumber() + ". : " + recorder.getPreview().getName());
            sceneColorMap.put(recorder.getPreview(), "orange");
        } else {
            tfPreview.setText("");
        }
        if (recorder.getActual() != null) {
            tfActual.setText("Scene " + recorder.getActual().getNumber() + ". : " + recorder.getActual().getName());
            sceneColorMap.put(recorder.getActual(), "green");

            int actualSceneIndex = sceneList.indexOf(recorder.getActual());
            if (actualSceneIndex != -1) {
                tableScenes.scrollTo(actualSceneIndex);
                tableScenes.getSelectionModel().select(actualSceneIndex);
            }
        } else {
            tfActual.setText("");
        }
        updateRowColor(sceneColorMap);
    }

    private void resetRecorderFields() {
        if(sceneList == null) {
            return;
        }
        for (Scene scene : sceneList) {
            if (recorder.getPreview() != null && scene.getId() == recorder.getPreview().getId()) {
                recorder.setPreview(scene);
            }
            if (recorder.getActual() != null && scene.getId() == recorder.getActual().getId()) {
                recorder.setActual(scene);
            }
        }
        showRecorderFields();
    }

    private boolean validateSceneList() {
        return this.sceneList != null && !this.sceneList.isEmpty();
    }

//    Scene elements

    @FXML
    private TableView<Scene> tableScenes;

    @FXML
    private TableColumn<Scene, Integer> colNumScene;

    @FXML
    private TableColumn<Scene, String> colNameScene;

    @FXML
    private TableColumn<Scene, String> colDescrSene;

    @FXML
    private TableColumn<Scene, String> colCamName;

    @FXML
    private TableColumn<Scene, String> colCamDescription;

    @FXML
    private Button btnClearSceneFields;

    @FXML
    private Button btnDeleteScene;

    @FXML
    private Button btnSaveScene;

    @FXML
    private Button btnUpdateScene;

    @FXML
    private TextField tfNumScene;

    @FXML
    private TextField tfNameScene;

    @FXML
    private TextField tfDescrScene;

    @FXML
    private ChoiceBox<String> cbCamName;

    @FXML
    void clearFieldsScene(ActionEvent event) {
        clearScene();
    }

    @FXML
    void deleteScene(ActionEvent event) {
        Scene selectedScene = tableScenes.getSelectionModel().getSelectedItem();
        if (selectedScene != null) {
            if (recorder.getPreview() != null && selectedScene == recorder.getPreview()) {
                Alarm.showError("Can't delete scene that is in Preview.");
            } else if (recorder.getActual() != null && selectedScene == recorder.getActual()) {
                Alarm.showError("Can't delete scene that is in Actual.");
            } else {
                if (Alarm.confirmationDelete(selectedScene.getNumber(), selectedScene.getName())) {
                    try {
                        sceneService.deleteScene(selectedScene);
                        showScenes();
                        clearScene();
                    } catch (NumberFormatException e) {
                        logger.error("Error caught in deleteScene: " + e.getMessage(), e);
                        Alarm.showError("Error in deleting scene.");
                    }
                }
            }


        }

    }

    @FXML
    void createScene(ActionEvent event) {
        if (validateSceneTextFields()) {
            try {
                sceneService.createScene(tfNumScene.getText().trim(), tfNameScene.getText().trim(), tfDescrScene.getText(), getCamNameFromCB(cbCamName.getValue()), SelectedSequence.getSelectedSequence());
                showScenes();
                clearScene();
            } catch (RuntimeException e) {
                logger.error("Error caught in createScene: " + e.getMessage(), e);
                Alarm.showError("Error in creating scene. \n" + e.getMessage());
            }
        }
    }

    @FXML
    void updateScene(ActionEvent event) {
        if (validateSceneTextFields()) {
            Scene selectedScene = tableScenes.getSelectionModel().getSelectedItem();
            try {
                if (selectedScene != null) {
                    sceneService.updateScene(tfNumScene.getText().trim(), tfNameScene.getText().trim(),
                            tfDescrScene.getText(), getCamNameFromCB(cbCamName.getValue()), selectedScene);
                    showScenes();
                    clearScene();
                }
            } catch (RuntimeException e) {
                logger.error("Error caught in updateScene: " + e.getMessage(), e);
                Alarm.showError("Error in updating scene.\n" + e.getMessage());
            }
        } else {
            Alarm.showError("Invalid input in fields.");
        }
    }

    private String getCamNameFromCB(String nameAndDescr) {
        int spaceIndex = nameAndDescr.indexOf(" ");
        return spaceIndex != -1 ? nameAndDescr.substring(0, spaceIndex): nameAndDescr;
    }

    @FXML
    Scene getSelectedSceneData() {
        Scene selectedScene = tableScenes.getSelectionModel().getSelectedItem();
        if (selectedScene != null) {
            tfNumScene.setText(String.valueOf(selectedScene.getNumber()));
            tfNameScene.setText(selectedScene.getName());
            tfDescrScene.setText(selectedScene.getDescription());
            cbCamName.setValue(selectedScene.getCamera().getName());
            btnSaveScene.setDisable(true);
        }
        return selectedScene;
    }

    private void showScenes() {
        clearScene();
        this.sceneList = sceneService.getScenesBySeqId();
        FXCollections.sort(sceneList, Comparator.comparing(Scene::getNumber));
        resetRecorderFields();
        populateCameraChoiceBox();
        try {
            tableScenes.setItems(sceneList);
            colNumScene.setCellValueFactory(new PropertyValueFactory<>("Number"));
            colNameScene.setCellValueFactory(new PropertyValueFactory<>("Name"));
            colDescrSene.setCellValueFactory(new PropertyValueFactory<>("Description"));
            colCamName.setCellValueFactory(cellData -> {
                MyCamera camera = cellData.getValue().getCamera();
                return new SimpleStringProperty(camera != null ? camera.getName() : "");
            });
            colCamDescription.setCellValueFactory(cellData -> {
                MyCamera camera = cellData.getValue().getCamera();
                return new SimpleStringProperty(camera != null ? camera.getDescription() : "No value");
            });


            tableScenes.getSelectionModel().clearSelection();
        } catch (Exception e) {
            logger.error("Error caught in showScenes: " + e.getMessage(), e);
            Alarm.showError("Error in showing scenes.");
        }

    }

    private void updateRowColor(Map<Scene, String> sceneColorMap) {
        tableScenes.setRowFactory(tv -> new TableRow<Scene>() {
            @Override
            protected void updateItem(Scene item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    String color = sceneColorMap.get(item);
                    if (color != null) {
                        setStyle("-fx-background-color:" + color + ";");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    private void populateCameraChoiceBox() {
        List<String> cameraNames = new ArrayList<>();
        for (MyCamera camera : myCameraService.getCamerasBySeqId()) {
//            Leave space between name and description: getCamNameFromCB
            cameraNames.add(camera.getName() + " " + camera.getDescription());
        }
        cbCamName.setItems(FXCollections.observableArrayList(cameraNames).sorted());
        if (getSelectedSceneData() != null) {
            cbCamName.setValue(getSelectedSceneData().getCamera().getName());
        } 
    }



    private void clearScene() {
        tfNumScene.setText("");
        tfNameScene.setText("");
        tfDescrScene.setText("");
        cbCamName.setValue("");
        btnSaveScene.setDisable(false);
        tableScenes.getSelectionModel().clearSelection();
    }

    private boolean validateSceneTextFields() {
        if (tfNumScene.getText().isBlank() || tfNameScene.getText().isBlank() ||
                tfDescrScene.getText().isBlank() || cbCamName.getValue().isBlank()) {
            Alarm.showError("No empty fields allowed.");
            return false;
        }
        try {
            int number = Integer.parseInt(tfNumScene.getText().trim());
            if (number <= 0) {
                Alarm.showError("Number must be higher than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            logger.error("Error caught in validateSceneTextFields: " + e.getMessage(), e);
            Alarm.showError("Invalid number input: " + tfNumScene.getText());
            return false;
        }
        return true;
    }


//    Cam elements

    @FXML
    private Button btnImportCams;

    @FXML
    private Button btnDeleteAllCams;

    @FXML
    private Button btnDeleteCam;

    @FXML
    private Button btnSaveCam;

    @FXML
    private Button btnUpdateCam;

    @FXML
    private Button btnClearCam;

    @FXML
    private TextField tfNameCam;

    @FXML
    private TextField tfDescriptionCam;

    @FXML
    private TableColumn<Camera, String> colNameCam;

    @FXML
    private TableColumn<Camera, String> colDescriptionCam;

    @FXML
    private TableView<MyCamera> tableCams;

    private void showCameras() {
        ObservableList<MyCamera> myCameraList = myCameraService.getCamerasBySeqId();
        FXCollections.sort(myCameraList, Comparator.comparing(MyCamera::getName));
        try {
            tableCams.setItems(myCameraList);
            colNameCam.setCellValueFactory(new PropertyValueFactory<>("Name"));
            colDescriptionCam.setCellValueFactory(new PropertyValueFactory<>("Description"));
        } catch (Exception e) {
            logger.error("Error caught in showCameras: " + e.getMessage(), e);
            Alarm.showError("Error in showing cameras.");
        }
        showScenes();
    }

    @FXML
    void importCams() {
        try {
            List<String> cameraNamesFromImport = cameraImporter.importCameras();
            myCameraService.createCamerasFromList(cameraNamesFromImport);
        } catch (RuntimeException e) {
            logger.error("Error caught in importCams: " + e.getMessage(), e);
            Alarm.showError("Error in importing cameras.");
        }

        showCameras();
    }

    @FXML
    void deleteAllCams() {
        if(!Alarm.confirmationDelete("All non attached cameras.")) {
            return;
        }
        StringBuilder deleteResponse = myCameraService.deleteAllCams();
        if(!deleteResponse.isEmpty()) {
            Alarm.showError(deleteResponse.toString());
        }
        showCameras();
    }

    @FXML
    void getSelectedCamData(MouseEvent event) {
        MyCamera selectedCamera = tableCams.getSelectionModel().getSelectedItem();
        if (selectedCamera != null) {
            tfNameCam.setText(selectedCamera.getName());
            tfDescriptionCam.setText(selectedCamera.getDescription());
            btnSaveCam.setDisable(true);
        }
    }

    @FXML
    void clearFieldsCam(ActionEvent event) {
        clearCam();
    }

    @FXML
    void createCam(ActionEvent event) {
        if (validateCamTextFields()) {
            try {
                myCameraService.createCam(tfNameCam.getText().trim(), tfDescriptionCam.getText(), SelectedSequence.getSelectedSequence());
                showCameras();
                clearCam();
            } catch (RuntimeException e) {
                logger.error("Error caught in createCam: " + e.getMessage(), e);
                Alarm.showError("Error in creating new Camera.\n" + e.getMessage());
            }
        } else {
            Alarm.showError("Invalid input in fields.");
        }
    }

    @FXML
    void deleteCam(ActionEvent event) {
        MyCamera selectedCam = tableCams.getSelectionModel().getSelectedItem();
        if (selectedCam != null) {
            if (Alarm.confirmationDelete(selectedCam.getName())) {
                try {
                    myCameraService.deleteCam(selectedCam.getId());
                    showCameras();
                    clearCam();
                } catch (RuntimeException e) {
                    logger.error("Error caught in deleteCam: " + e.getMessage(), e);
                    Alarm.showError("Error in deleting camera.\n" + e.getMessage());
                }
            }
        }
    }


    @FXML
    void updateCam(ActionEvent event) {
        if (validateCamTextFields()) {
            MyCamera selectedCam = tableCams.getSelectionModel().getSelectedItem();
            try {
                if (selectedCam != null) {
                    myCameraService.updateCam(tfNameCam.getText().trim(), tfDescriptionCam.getText(), selectedCam);
                    showCameras();
                    showScenes();
                    clearCam();
                }
            } catch (RuntimeException e) {
                logger.error("Error caught in updateCam: " + e.getMessage(), e);
                Alarm.showError("Error in updating camera.\n" + e.getMessage());
            }
        } else {
            Alarm.showError("Invalid input in fields.");
        }
    }

    @FXML
    void switchToMain(ActionEvent event) {
        try {
            App.switchToMainScreen();
        } catch (IOException e) {
            logger.error("Error caught in switchToMain: " + e.getMessage(), e);
            Alarm.showError("Error in switching to Main Screen.");
        }
    }


    private void clearCam() {
        tfNameCam.setText("");
        tfDescriptionCam.setText("");
        btnSaveCam.setDisable(false);
    }


    private boolean validateCamTextFields() {
        return (!tfNameCam.getText().isBlank() && !tfNameCam.getText().contains(" "));
    }


    void setLabel() {
        lbSequence.setText("SEQUENCE: " + SelectedSequence.getSelectedSequence().getName());
    }
}
