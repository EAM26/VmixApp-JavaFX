package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.dao.SequenceDAO;
import org.eam.code.vmixapp.model.Sequence;

import java.util.ArrayList;
import java.util.List;

public class SequenceService {

    private final SequenceDAO sequenceDAO;
    private final MyCameraDAO cameraDAO;


    public SequenceService() {
        this.sequenceDAO = new SequenceDAO();
        this.cameraDAO = new MyCameraDAO();
    }


    public ObservableList<Sequence> getSequences() {
        return FXCollections.observableArrayList(sequenceDAO.getSequences());
    }

    public Sequence getSequenceById(int id) {
        return sequenceDAO.getSequenceById(id);
    }

    public void createSequence(String name, String description) {
        sequenceDAO.createSequence(name, description);
    }

    public void updateSequence(int id, String name, String description) {
        sequenceDAO.updateSequence(id, name, description);
    }

    public void deleteSequence(int id) {
        if (!cameraDAO.getCameras().isEmpty()) {
            throw new RuntimeException("Sequence has camera(s).");
        }
        sequenceDAO.deleteSequence(id);
    }

}
