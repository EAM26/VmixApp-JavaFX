package org.eam.code.vmixapp.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.eam.code.vmixapp.App;
import org.eam.code.vmixapp.DBConnection;
import org.eam.code.vmixapp.dao.MyCameraDAO;
import org.eam.code.vmixapp.dao.SequenceDAO;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.service.MyCameraService;
import org.eam.code.vmixapp.service.SequenceService;
import org.eam.code.vmixapp.util.Alarm;
import org.eam.code.vmixapp.util.SelectedSequence;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SequenceController implements Initializable {
    private final SequenceService sequenceService;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet resultSet = null;

    public SequenceController() {
        this.sequenceService = new SequenceService(new SequenceDAO(), new MyCameraService(new MyCameraDAO()));
    }

    @FXML
    private Button btnSwitch;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableView<Sequence> tableSeq;

    @FXML
    private TableColumn<Sequence, String> colDescription;

    @FXML
    private TableColumn<Sequence, Integer> colId;

    @FXML
    private TableColumn<Sequence, String> colName;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clear();
        showSequences();
    }


    @FXML
    private void getSelectedData(MouseEvent event) {
        SelectedSequence.setSelectedSequence(tableSeq.getSelectionModel().getSelectedItem());
        if (SelectedSequence.getSelectedSequence() != null) {
            tfName.setText(SelectedSequence.getSelectedSequence().getName());
            tfDescription.setText(SelectedSequence.getSelectedSequence().getDescription());
            btnSave.setDisable(true);
        }
    }


    @FXML
    void clearField(ActionEvent event) {
        clear();
    }

    @FXML
    void createSequence(ActionEvent event) {
        if(validateTextFields()) {
            String name = tfName.getText();
            String description = tfDescription.getText();
            try {
                sequenceService.createSequence(name, description);
                showSequences();
                clear();
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                Alarm.showError("Error in creating new sequence.");
            }
        }
    }

    @FXML
    void deleteSequence(ActionEvent event) {
        if(SelectedSequence.getSelectedSequence() != null) {
            if(Alarm.showAskConfirmation(SelectedSequence.getSelectedSequence().getId(), SelectedSequence.getSelectedSequence().getName())) {

                try {
                    sequenceService.deleteSequence(SelectedSequence.getSelectedSequence().getId());
                    showSequences();
                    clear();
                } catch (RuntimeException e) {
                    System.err.println(e.getMessage());
                    Alarm.showError("Error in deleting sequence.\n" + e.getMessage());
                }
            }

        }
    }

    @FXML
    void updateSequence(ActionEvent event) {
        if(SelectedSequence.getSelectedSequence() != null && validateTextFields()) {
            String updateMessage = "update sequences set Name=?, Description=? where id=?";
            con = DBConnection.getCon();
            try {
                pstmt = con.prepareStatement(updateMessage);
                pstmt.setString(1, tfName.getText());
                pstmt.setString(2, tfDescription.getText());
                pstmt.setInt(3, SelectedSequence.getSelectedSequence().getId());
                pstmt.executeUpdate();
                showSequences();
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showSequences() {
        try{
            ObservableList<Sequence> seqList = sequenceService.getSequences();
            tableSeq.setItems(seqList);
            colId.setCellValueFactory(new PropertyValueFactory<Sequence, Integer>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<Sequence, String>("name"));
            colDescription.setCellValueFactory(new PropertyValueFactory<Sequence, String>("description"));
        } catch (Exception e) {
            Alarm.showError("An error occurred while loading sequences: " + e.getMessage());
        }

    }

    private void clear() {
        tfName.setText("");
        tfDescription.setText("");
        SelectedSequence.setSelectedSequence(null);
        btnSave.setDisable(false);
    }


    private boolean validateTextFields() {
        return !tfName.getText().isBlank() && !tfDescription.getText().isBlank();
    }

    @FXML
    void switchToOP(ActionEvent event) {
        try {
            App.switchToOPScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
