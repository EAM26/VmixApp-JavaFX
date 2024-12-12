package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.dao.SceneDao;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.util.SelectedSequence;
import org.eam.code.vmixapp.util.Validation;

public class MyCameraService {
    private final MyCameraDAO cameraDAO;
    private final SceneDao sceneDao;

    public MyCameraService() {
        this.cameraDAO = new MyCameraDAO();
        this.sceneDao = new SceneDao();
    }

    public ObservableList<MyCamera> getCamerasBySeqId() {
        if (SelectedSequence.getSelectedSequence() != null) {
            int seqId = SelectedSequence.getSelectedSequence().getId();
            return FXCollections.observableArrayList(cameraDAO.getCamerasBySeqId(seqId));
        }
        return FXCollections.observableArrayList();
    }

    public void createCam(String ref, String name, Sequence sequence) {
        if (camRefExists(ref)) {
            throw new IllegalArgumentException("Reference camera is not unique.");
        }
        if (camNameExists(name)) {
            throw new IllegalArgumentException("Name camera is not unique.");
        }
        cameraDAO.createCamera(ref, name, sequence);
    }

    public void updateCam(String ref, String name, int id) {
        if (camRefExists(ref)) {
            throw new IllegalArgumentException("Reference camera is not unique.");
        }
        if (camNameExists(name)) {
            throw new IllegalArgumentException("Name camera is not unique.");
        }
        cameraDAO.updateCam(ref, name, id);

    }

    public void deleteCam(int id) {
        if (!camHasScene(id)) {
            cameraDAO.deleteCam(id);
        }
    }


    private boolean camHasScene(int id) {
        if (Validation.existsInTable("scenes", "CamId", id)) {
            throw new IllegalStateException("Scene present.");
        }
        return false;
    }

    private boolean camNameExists(String camName) {
        return Validation.existsInTable("cameras", "name", camName);
    }

    private boolean camRefExists(String camRef) {
        return Validation.existsInTable("cameras", "ref", camRef);
    }
}
