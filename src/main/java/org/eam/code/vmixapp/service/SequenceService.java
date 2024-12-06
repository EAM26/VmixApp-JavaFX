package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.SequenceDAO;
import org.eam.code.vmixapp.model.Sequence;

import java.util.ArrayList;
import java.util.List;

public class SequenceService {

    public final SequenceDAO sequenceDAO;

    public SequenceService(SequenceDAO sequenceDAO) {
        this.sequenceDAO = sequenceDAO;
    }


    public ObservableList<Sequence> getSequences() {
        return FXCollections.observableArrayList(sequenceDAO.getSequences());


    }

}
