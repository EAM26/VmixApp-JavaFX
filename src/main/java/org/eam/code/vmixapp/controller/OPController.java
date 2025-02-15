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

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class OPController implements Initializable {
    String xmlResponse = "<vmix><version>28.0.0.36</version><edition>4K</edition><preset>C:\\Users\\synco\\AppData\\Roaming\\last.vmix</preset><inputs>"
            + "<input key=\"4d64cadf-aa54-461b-8e78-97ea41108da2\" number=\"1\" type=\"Capture\" title=\"C1\" shortTitle=\"C1\" state=\"Running\" position=\"0\" duration=\"0\" loop=\"False\">C1</input>"
            + "<input key=\"652898f9-a524-40ba-a21f-c0b28d512a0c\" number=\"2\" type=\"Colour\" title=\"C2\" shortTitle=\"C2\" state=\"Paused\" position=\"0\" duration=\"0\" loop=\"False\">C2</input>"
            + "<input key=\"bbc3da0b-09ee-4ed7-90b4-97ba106f872a\" number=\"3\" type=\"Virtual\" title=\"PTZ - C1-TOTAAL\" shortTitle=\"C1-TOTAAL\" state=\"Paused\" position=\"0\" duration=\"0\" loop=\"False\">PTZ - C1-TOTAAL</input>"
            + "<input key=\"db907683-92f5-4a59-854f-aa48c529e6c3\" number=\"4\" type=\"Virtual\" title=\"PTZ - C1-HOUT\" shortTitle=\"C1-HOUT\" state=\"Paused\" position=\"0\" duration=\"0\" loop=\"False\">PTZ - C1-HOUT</input>"
            + "<input key=\"680decec-39e0-47e4-8c73-9049ae549d01\" number=\"5\" type=\"Image\" title=\"cam10-Piano-Superclose.jpg\" shortTitle=\"cam10-Piano-Superclose.jpg\" state=\"Paused\" position=\"0\" duration=\"0\" loop=\"False\">cam10-Piano-Superclose.jpg</input>"
            + "</inputs></vmix>";

    private final MyCameraService myCameraService;
    private final SceneService sceneService;
    private final Recorder recorder;
    private final VMRequest request;
    private final CameraImporter cameraImporter;


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
                System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
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

//    @FXML
//    private TableColumn<Scene, String> colCamRef;


    @FXML
    private TableColumn<Scene, String> colCamName;

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

//    @FXML
//    private TextField tfCamRef;

//    @FXML
//    private TextField tfCamName;

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
                        System.err.println(e.getMessage());
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
//                sceneService.createScene(tfNumScene.getText().trim(), tfNameScene.getText().trim(), tfDescrScene.getText(), tfCamRef.getText().trim(), SelectedSequence.getSelectedSequence());
                sceneService.createScene(tfNumScene.getText().trim(), tfNameScene.getText().trim(), tfDescrScene.getText(), cbCamName.getValue(), SelectedSequence.getSelectedSequence());
                showScenes();
                clearScene();
            } catch (RuntimeException e) {
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
//                            tfDescrScene.getText(), tfCamRef.getText().trim(), selectedScene);
                            tfDescrScene.getText(), cbCamName.getValue(), selectedScene);
                    showScenes();
                    clearScene();
                }
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                Alarm.showError("Error in updating scene.\n" + e.getMessage());
            }
        } else {
            Alarm.showError("Invalid input in fields.");
        }
    }

    @FXML
    Scene getSelectedSceneData() {
        Scene selectedScene = tableScenes.getSelectionModel().getSelectedItem();
        if (selectedScene != null) {
            tfNumScene.setText(String.valueOf(selectedScene.getNumber()));
            tfNameScene.setText(selectedScene.getName());
            tfDescrScene.setText(selectedScene.getDescription());
//            tfCamRef.setText(selectedScene.getCamera().getRef());
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
//            colCamRef.setCellValueFactory(cellData -> {
//                MyCamera camera = cellData.getValue().getCamera();
//                return new SimpleStringProperty(camera != null ? camera.getRef() : "");
//            });

            colCamName.setCellValueFactory(cellData -> {
                MyCamera camera = cellData.getValue().getCamera();
                return new SimpleStringProperty(camera != null ? camera.getName() : "");
            });

            tableScenes.getSelectionModel().clearSelection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        for (MyCamera cameraName : myCameraService.getCamerasBySeqId()) {
            cameraNames.add(cameraName.getName());
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
//        tfCamRef.setText("");
//        tfCamName.setText("");
        cbCamName.setValue("");
        btnSaveScene.setDisable(false);
        tableScenes.getSelectionModel().clearSelection();
    }

    private boolean validateSceneTextFields() {
        if (tfNumScene.getText().isBlank() || tfNameScene.getText().isBlank() ||
//                tfDescrScene.getText().isBlank() || tfCamRef.getText().isBlank()) {
//                tfDescrScene.getText().isBlank() || tfCamName.getText().isBlank()) {
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
            System.err.println(e.getMessage());
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

//    @FXML
//    private TextField tfRef;

    @FXML
    private TextField tfNameCam;

    @FXML
    private TableColumn<Camera, String> colNameCam;

    @FXML
    private TableColumn<Camera, Integer> colRef;

    @FXML
    private TableView<MyCamera> tableCams;

    private void showCameras() {
        ObservableList<MyCamera> myCameraList = myCameraService.getCamerasBySeqId();
//        FXCollections.sort(myCameraList, Comparator.comparing(MyCamera::getRef));
        FXCollections.sort(myCameraList, Comparator.comparing(MyCamera::getName));
        try {
            tableCams.setItems(myCameraList);
//            colRef.setCellValueFactory(new PropertyValueFactory<>("Ref"));
            colNameCam.setCellValueFactory(new PropertyValueFactory<>("Name"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        showScenes();
    }

    @FXML
    void importCams() {
        List<String> cameraNamesFromImport = cameraImporter.importCameras();
        System.out.println(cameraNamesFromImport);
        myCameraService.createCamerasFromList(cameraNamesFromImport);
        showCameras();
    }

    @FXML
    void deleteAllCams() {
        myCameraService.deleteAllCams();
        showCameras();
    }

    @FXML
    void getSelectedCamData(MouseEvent event) {
        MyCamera selectedCamera = tableCams.getSelectionModel().getSelectedItem();
        if (selectedCamera != null) {
//            tfRef.setText(String.valueOf(selectedCamera.getRef()));
            tfNameCam.setText(selectedCamera.getName());
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
//                myCameraService.createCam(tfRef.getText().trim(), tfNameCam.getText().trim(), SelectedSequence.getSelectedSequence());
                myCameraService.createCam(tfNameCam.getText().trim(), SelectedSequence.getSelectedSequence());
                showCameras();
                clearCam();
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
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
//            if (Alarm.confirmationDelete(selectedCam.getRef(), selectedCam.getName())) {
            if (Alarm.confirmationDelete(selectedCam.getName())) {
                try {
                    myCameraService.deleteCam(selectedCam.getId());
                    showCameras();
                    clearCam();
                } catch (RuntimeException e) {
                    System.err.println(e.getMessage());
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
//                    myCameraService.updateCam(tfRef.getText().trim(), tfNameCam.getText().trim(), selectedCam);
                    myCameraService.updateCam(tfNameCam.getText().trim(), selectedCam);
                    showCameras();
                    showScenes();
                    clearCam();
                }
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
            Alarm.showError("Error in switching to Main Screen.");
        }
    }


    private void clearCam() {
//        tfRef.setText("");
        tfNameCam.setText("");
        btnSaveCam.setDisable(false);
    }


    private boolean validateCamTextFields() {
//        return !tfRef.getText().isBlank() && !tfNameCam.getText().isBlank();
        return !tfNameCam.getText().isBlank();
    }


    void setLabel() {
        lbSequence.setText("SEQUENCE: " + SelectedSequence.getSelectedSequence().getName());
    }
}
