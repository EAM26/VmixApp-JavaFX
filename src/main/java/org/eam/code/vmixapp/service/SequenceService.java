package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.SequenceDAO;
import org.eam.code.vmixapp.model.Sequence;

import java.util.ArrayList;
import java.util.List;

public class SequenceService {

    private final SequenceDAO sequenceDAO;
    private final MyCameraService myCameraService;


    public SequenceService(SequenceDAO sequenceDAO, MyCameraService myCameraService) {
        this.sequenceDAO = sequenceDAO;
        this.myCameraService = myCameraService;
    }


    public ObservableList<Sequence> getSequences() {
        return FXCollections.observableArrayList(sequenceDAO.getSequences());
    }

    public void createSequence(String name, String description) {
        sequenceDAO.createSequence(name, description);
    }

    public void deleteSequence(int id) {
        if(!myCameraService.getCameras().isEmpty()) {
            throw new RuntimeException("Sequence has camera(s).");
        }
        sequenceDAO.deleteSequence(id);
    }

}
