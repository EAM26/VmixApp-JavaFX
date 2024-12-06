package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.CameraDAO;
import org.eam.code.vmixapp.model.Camera;

public class CameraService {
    private final CameraDAO cameraDAO;

    public CameraService(CameraDAO cameraDAO) {
        this.cameraDAO = cameraDAO;
    }

    public ObservableList<Camera> getCameras() {
        return FXCollections.observableArrayList(cameraDAO.getCameras());
    }
}
