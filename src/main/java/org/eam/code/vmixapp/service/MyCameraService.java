package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Sequence;

public class MyCameraService {
    private final MyCameraDAO cameraDAO;

    public MyCameraService() {
        this.cameraDAO = new MyCameraDAO();
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
        cameraDAO.deleteCam(id);
    }

    public MyCamera getCameraById(int id) {
        return cameraDAO.getCameraById(id);
    }
}
