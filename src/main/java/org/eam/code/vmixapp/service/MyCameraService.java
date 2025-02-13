package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.dao.SceneDao;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.util.SelectedSequence;
import org.eam.code.vmixapp.util.Validation;

import java.util.ArrayList;
import java.util.List;

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

//    public void createCam(String ref, String name, Sequence sequence) {
    public void createCam(String name, Sequence sequence) {
//        if (camRefExists(ref)) {
//            throw new IllegalArgumentException("Reference camera is not unique.");
//        }
        if (camNameExists(name)) {
            throw new IllegalArgumentException("Name camera is not unique.");
        }
//        cameraDAO.createCamera(ref, name, sequence);
        cameraDAO.createCamera(name, sequence);
    }

    public void createCamerasFromList(List<String> importedCams, Sequence sequence) {
        importedCams.removeAll(getNamesPresentCameras());
        for(String camName: importedCams) {
            createCam(camName, sequence);
        }
    }

//    public void updateCam(String ref, String name, MyCamera selectedCam) {
    public void updateCam(String name, MyCamera selectedCam) {
//        if (!ref.equals(selectedCam.getRef())) {
//            if (camRefExists(ref)) {
//                throw new IllegalArgumentException("Reference camera is not unique.");
//            }
//        }
        if (!name.equals(selectedCam.getName())) {
            if (camNameExists(name)) {
                throw new IllegalArgumentException("Name camera is not unique.");
            }
        }
//        cameraDAO.updateCam(ref, name, selectedCam.getId());
        cameraDAO.updateCam(name, selectedCam.getId());

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

    public boolean camNameExists(String camName) {
        return Validation.existsInTable("cameras", "name", camName, "SeqId", SelectedSequence.getSelectedSequence().getId());
    }

    public boolean camRefExists(String camRef) {
        return Validation.existsInTable("cameras", "ref", camRef, "SeqId", SelectedSequence.getSelectedSequence().getId());
    }

    private List<String> getNamesPresentCameras() {
        List<String> presentCamNames = new ArrayList<>();
        for(MyCamera camera: getCamerasBySeqId()) {
            presentCamNames.add(camera.getName());
        }
        return presentCamNames;
    }
}
