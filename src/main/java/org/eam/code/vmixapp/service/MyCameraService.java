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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void createCam(String name, String description, Sequence sequence) {
//        if (camRefExists(ref)) {
//            throw new IllegalArgumentException("Reference camera is not unique.");
//        }
        if (camNameExists(name)) {
            throw new IllegalArgumentException("Name camera is not unique: " + name);
        }
//        cameraDAO.createCamera(ref, name, sequence);
        cameraDAO.createCamera(name, description, sequence);
    }

    public void createCamerasFromList(List<String> importedCams) {
        Sequence selectedSequence = SelectedSequence.getSelectedSequence();
        importedCams.removeAll(getNamesPresentCameras());
        Set<String> uniqueImportedCams = new HashSet<>(importedCams);
        for(String camName: uniqueImportedCams) {
            createCam(camName, "No Description", selectedSequence);
        }
    }

    public void updateCam(String name, String description,  MyCamera selectedCam) {
        if (!name.equals(selectedCam.getName())) {
            if (camNameExists(name)) {
                throw new IllegalArgumentException("Name camera is not unique.");
            }
        }
        cameraDAO.updateCam(name, description, selectedCam.getId());

    }

    public void deleteCam(int id) {
        if (!camHasScene(id)) {
            cameraDAO.deleteCam(id);
        }
    }

    public StringBuilder deleteAllCams() {
        StringBuilder errorMessage = new StringBuilder();
        int seqId = SelectedSequence.getSelectedSequence().getId();
        List<MyCamera> allCameras = cameraDAO.getCamerasBySeqId(seqId);
        for(MyCamera camera: allCameras) {
            try {
            deleteCam(camera.getId());
            } catch (RuntimeException e) {
                System.err.println(camera.getName() + " has scene attached.");
                errorMessage.append(camera.getName()).append("has scene attached.\n");
            }

        }
        return errorMessage;
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
