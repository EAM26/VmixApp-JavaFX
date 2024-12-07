package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.util.SelectedSequence;

public class MyCameraService {
    private final MyCameraDAO cameraDAO;

    public MyCameraService(MyCameraDAO cameraDAO) {
        this.cameraDAO = cameraDAO;
    }

    public ObservableList<MyCamera> getCameras() {
        return FXCollections.observableArrayList(cameraDAO.getCameras());
    }

    public void createCam(String ref, String name, Sequence sequence) {
        cameraDAO.createCamera(ref, name, sequence);
    }
}
