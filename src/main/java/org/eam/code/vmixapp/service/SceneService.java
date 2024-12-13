package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.dao.SceneDao;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Scene;
import org.eam.code.vmixapp.model.Sequence;
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

    public void createScene(String sceneNumberAsString, String sceneName, String sceneDescription, String camRef, Sequence sequence) {
        int sceneNumber = Integer.parseInt(sceneNumberAsString.trim());

        if (sceneNumberExists(sceneNumber)) {
            throw new IllegalArgumentException("Scene number: " + sceneNumber + " already exists.");
        }
        if (sceneNameExists(sceneName)) {
            throw new IllegalArgumentException("Scene name: "  + sceneName + " already exists.");
        }
        if (!myCameraService.camRefExists(camRef)) {
            throw new IllegalArgumentException(camRef + " doesnt exist as camera reference.");
        }
        MyCamera camera = myCameraDAO.getCameraByRef(camRef);
        sceneDao.createScene(sceneNumber, sceneName, sceneDescription, camera , sequence);
    }

    public void updateScene(String sceneNumberAsString, String sceneName, String sceneDescription,
                            String camRef, Scene selectedScene) {
        int sceneNumber = Integer.parseInt(sceneNumberAsString.trim());
        if (sceneNumber != selectedScene.getNumber()) {
            if(sceneNumberExists(sceneNumber)) {
                throw new IllegalArgumentException("Scene number is not unique.");
            }
        }
        if(!sceneName.equals(selectedScene.getName())) {
            if(sceneNameExists(sceneName)) {
                throw new IllegalArgumentException("Scene name is not unique");
            }
        }
        if (!myCameraService.camRefExists(camRef)) {
            throw new IllegalArgumentException(camRef + " doesnt exist as camera reference.");
        }
        int camId = myCameraDAO.getCameraByRef(camRef).getId();
        sceneDao.updateScene(sceneNumber, sceneName, sceneDescription, camId, selectedScene.getId());
    }



    private boolean sceneNumberExists(int sceneNum) {
        return Validation.existsInTable("scenes", "Number", sceneNum, "SeqId", SelectedSequence.getSelectedSequence().getId());

    }

    private boolean sceneNameExists(String sceneName) {
        return Validation.existsInTable("scenes", "Name", sceneName, "SeqId", SelectedSequence.getSelectedSequence().getId());
    }

}
