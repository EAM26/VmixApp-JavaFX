package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.dao.SceneDao;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Sequence;

public class MyCameraService {
    private final MyCameraDAO cameraDAO;
    private final SceneDao sceneDao;

    public MyCameraService() {
        this.cameraDAO = new MyCameraDAO();
        this.sceneDao = new SceneDao();
    }

    public ObservableList<MyCamera> getCameras() {
        return FXCollections.observableArrayList(cameraDAO.getCameras());
    }

    public void createCam(String ref, String name, Sequence sequence) {
        cameraDAO.createCamera(ref, name, sequence);
    }

    public void updateCam(String ref, String name, int id) {
        cameraDAO.updateCam(ref, name, id);
    }

    public void deleteCam(int id) {
        if (!sceneDao.getScenes().isEmpty()) {
            throw new IllegalStateException("Camera has scene(s).");
        }
        cameraDAO.deleteCam(id);
    }
}
