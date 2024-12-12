package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.SceneDao;
import org.eam.code.vmixapp.model.Scene;
import org.eam.code.vmixapp.util.SelectedSequence;
import org.eam.code.vmixapp.util.Validation;

public class SceneService {
    private final SceneDao sceneDao;
    private final MyCameraService myCameraService;

    public SceneService() {
        this.sceneDao = new SceneDao();
        this.myCameraService = new MyCameraService();
    }

    public ObservableList<Scene> getScenesBySeqId() {
        if (SelectedSequence.getSelectedSequence() != null) {
            int seqId = SelectedSequence.getSelectedSequence().getId();
            return FXCollections.observableArrayList(sceneDao.getScenesBySeqId(seqId));
        }
        return FXCollections.observableArrayList();
    }

    public void deleteScene(int id) {
        sceneDao.deleteScene(id);
    }

    public void createScene(String sceneNumberAsString, String sceneName, String sceneDescription, String camRef) {
        int sceneNumber = Integer.parseInt(sceneNumberAsString.trim());
        if (!myCameraService.camRefExists(camRef)) {
            throw new IllegalArgumentException(camRef + " doesnt exist as camera reference.");
        }
        if (sceneNumberExists(sceneNumber)) {
            throw new IllegalArgumentException(sceneNumber + " already exists.");
        }
        if (sceneNameExists(sceneName)) {
            throw new IllegalArgumentException(sceneName + " already exists.");
        }
        System.out.println("Scene created.");

    }

    private boolean sceneNumberExists(int sceneNum) {
        return Validation.existsInTable("scenes", "Number", sceneNum, "SeqId", SelectedSequence.getSelectedSequence().getId());

    }

    private boolean sceneNameExists(String sceneName) {
        return Validation.existsInTable("scenes", "Name", sceneName, "SeqId", SelectedSequence.getSelectedSequence().getId());
    }

}
