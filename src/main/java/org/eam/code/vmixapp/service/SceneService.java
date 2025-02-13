package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.dao.SceneDao;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Scene;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.util.Alarm;
import org.eam.code.vmixapp.util.SelectedSequence;
import org.eam.code.vmixapp.util.Validation;

public class SceneService {
    private final SceneDao sceneDao;
    private final MyCameraService myCameraService;
    private final MyCameraDAO myCameraDAO;

    public SceneService() {
        this.sceneDao = new SceneDao();
        this.myCameraService = new MyCameraService();
        this.myCameraDAO = new MyCameraDAO();
    }

    public ObservableList<Scene> getScenesBySeqId() {
        if (SelectedSequence.getSelectedSequence() != null) {
            int seqId = SelectedSequence.getSelectedSequence().getId();
            return FXCollections.observableArrayList(sceneDao.getScenesBySeqId(seqId));
        }
        return FXCollections.observableArrayList();
    }

    public void deleteScene(Scene scene) {
        sceneDao.deleteScene(scene.getId());
        sceneDao.sceneNumDecrement(scene.getNumber());
    }

    //    public void createScene(String sceneNumberAsString, String sceneName, String sceneDescription, String camRef, Sequence sequence) {
    public void createScene(String sceneNumberAsString, String sceneName, String sceneDescription, String camName, Sequence sequence) {
        if (sceneNameExists(sceneName)) {
            throw new IllegalArgumentException("Scene name: " + sceneName + " already exists.");
        }
        if (!myCameraService.camNameExists(camName)) {
            throw new IllegalArgumentException(camName + " doesnt exist as camera reference.");
        }
        int sceneNumber = Integer.parseInt(sceneNumberAsString.trim());
        if (sceneNumberExists(sceneNumber)) {
            if (Alarm.confirmationInsert(sceneNumber, sceneName)) {
                sceneDao.sceneNumIncrement(sceneNumber);
            } else {
                return;
            }
        }
//        MyCamera camera = myCameraDAO.getCameraByRef(camRef);
        MyCamera camera = myCameraDAO.getCameraByName(camName);
        sceneDao.createScene(sceneNumber, sceneName, sceneDescription, camera, sequence);
    }

    //    public void updateScene(String sceneNumberAsString, String sceneName, String sceneDescription,
//                            String camRef, Scene selectedScene) {
    public void updateScene(String sceneNumberAsString, String sceneName, String sceneDescription,
                            String camName, Scene selectedScene) {
        if (!sceneName.equals(selectedScene.getName())) {
            if (sceneNameExists(sceneName)) {
                throw new IllegalArgumentException("Scene name is not unique");
            }
        }
//        if (!myCameraService.camRefExists(camRef)) {
//            throw new IllegalArgumentException(camRef + " doesnt exist as camera reference.");
//        }
        if (!myCameraService.camNameExists(camName)) {
            throw new IllegalArgumentException(camName + " doesnt exist as camera name.");
        }
//        int camId = myCameraDAO.getCameraByRef(camRef).getId();
        int camId = myCameraDAO.getCameraByName(camName).getId();

        int sceneNumber = Integer.parseInt(sceneNumberAsString.trim());
        if (sceneNumber == selectedScene.getNumber()) {
            sceneDao.updateScene(sceneNumber, sceneName, sceneDescription, camId, selectedScene.getId());
            return;
        }
        if (sceneNumberExists(sceneNumber)) {
            if (Alarm.confirmationInsert(sceneNumber, sceneName)) {
                sceneDao.sceneNumIncrement(sceneNumber);
            } else {
                return;
            }
        }
        sceneDao.updateScene(sceneNumber, sceneName, sceneDescription, camId, selectedScene.getId());
        sceneDao.sceneNumDecrement(selectedScene.getNumber());
    }


    private boolean sceneNumberExists(int sceneNum) {
        return Validation.existsInTable("scenes", "Number", sceneNum, "SeqId", SelectedSequence.getSelectedSequence().getId());

    }

    private boolean sceneNameExists(String sceneName) {
        return Validation.existsInTable("scenes", "Name", sceneName, "SeqId", SelectedSequence.getSelectedSequence().getId());
    }

}
