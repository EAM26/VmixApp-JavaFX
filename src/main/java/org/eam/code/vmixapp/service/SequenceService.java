package org.eam.code.vmixapp.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.dao.SceneDao;
import org.eam.code.vmixapp.dao.SequenceDAO;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.util.SelectedSequence;
import org.eam.code.vmixapp.util.Validation;

import java.nio.channels.SelectableChannel;

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

    public void createSequence(String name, String description, String ipAddress, String port) {
        if(seqNameExists(name)) {
            throw new IllegalArgumentException("Name for sequence not unique.");
        }
        sequenceDAO.createSequence(name, description, ipAddress, port);
    }

    public void updateSequence(int id, String name, String description) {
        if(!name.equals(SelectedSequence.getSelectedSequence().getName()) && seqNameExists(name)) {
            throw new IllegalArgumentException("Name for sequence not unique.");
        }
        sequenceDAO.updateSequence(id, name, description);
    }

    public void deleteSequence(int id) {
        if (sequenceHasScene(id)) {
            throw new IllegalStateException("Scene present.");
        }
        if(sequenceHasCamera(id)) {
            throw new IllegalStateException("Camera present.");
        }
        sequenceDAO.deleteSequence(id);
    }

    private boolean sequenceHasScene(int id) {
        return Validation.existsInTable("scenes", "SeqId", id);
    }

    private boolean sequenceHasCamera(int id) {
        return Validation.existsInTable("cameras", "SeqId", id);
    }

    private boolean seqNameExists(String seqName) {
        return Validation.existsInTable("sequences", "Name", seqName);

    }




}
