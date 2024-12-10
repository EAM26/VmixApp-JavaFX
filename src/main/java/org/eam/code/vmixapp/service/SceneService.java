package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.SceneDao;
import org.eam.code.vmixapp.model.Scene;

public class SceneService {
    private final SceneDao sceneDao;

    public SceneService() {
        this.sceneDao = new SceneDao();
    }

    public ObservableList<Scene> getScenes() {
        return FXCollections.observableArrayList(sceneDao.getScenes());
    }

    public void deleteScene(int id) {
        sceneDao.deleteScene(id);
    }
}
