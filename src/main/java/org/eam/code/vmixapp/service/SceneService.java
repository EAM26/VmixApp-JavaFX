package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.SceneDao;
import org.eam.code.vmixapp.model.Scene;
import org.eam.code.vmixapp.util.SelectedSequence;
import org.eam.code.vmixapp.util.Validation;

public class SceneService {
    private final SceneDao sceneDao;

    public SceneService() {
        this.sceneDao = new SceneDao();
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

    public void createScene(String sceneNumber, String sceneName, String sceneDescription, String camRef) {

    }

    private boolean sceneNumberExists(int sceneNum){
        return Validation.existsInTable("scenes", "Number", sceneNum);

    }

    private boolean sceneNameExists(String sceneName){
        return Validation.existsInTable("scenes", "Name", sceneName);
    }

}
