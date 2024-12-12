package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.dao.SceneDao;
import org.eam.code.vmixapp.dao.SequenceDAO;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.util.Validation;

public class SequenceService {

    private final SequenceDAO sequenceDAO;
    private final MyCameraDAO cameraDAO;
    private final SceneDao sceneDao;


    public SequenceService() {
        this.sequenceDAO = new SequenceDAO();
        this.cameraDAO = new MyCameraDAO();
        this.sceneDao = new SceneDao();
    }

    public ObservableList<Sequence> getSequences() {
        return FXCollections.observableArrayList(sequenceDAO.getSequences());
    }

    public void createSequence(String name, String description) {
        sequenceDAO.createSequence(name, description);
    }

    public void updateSequence(int id, String name, String description) {
        sequenceDAO.updateSequence(id, name, description);
    }

    public void deleteSequence(int id) {
        if(!sequenceHasScene(id) && !sequenceHasCamera(id)) {
            sequenceDAO.deleteSequence(id);
        }
    }

    private boolean sequenceHasScene(int id) {
        if (Validation.existsInTable("scenes", "SeqId", id)) {
            throw new IllegalStateException("Scene present.");
        }
        return false;
    }

    private boolean sequenceHasCamera(int id) {
        if (Validation.existsInTable("cameras", "SeqId", id)) {
            throw new IllegalStateException("Camera present.");
        }
        return false;
    }


}
