package org.eam.code.vmixapp.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.eam.code.vmixapp.App;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.service.SequenceService;
import org.eam.code.vmixapp.util.Alarm;
import org.eam.code.vmixapp.util.SelectedSequence;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SequenceController implements Initializable {
    private final SequenceService sequenceService;

    public SequenceController() {
        this.sequenceService = new SequenceService();
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
    private TableColumn<Sequence, String> colPort;

    @FXML
    private TableColumn<Sequence, String> colIPAddress;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfIPAddress;


    @FXML
    private TextField tfPort;

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
            tfIPAddress.setText(SelectedSequence.getSelectedSequence().getIpAddress());
            tfPort.setText(SelectedSequence.getSelectedSequence().getPort());
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
            String name = tfName.getText().trim();
            String description = tfDescription.getText();
            String ipAddress = tfIPAddress.getText();
            String port = tfPort.getText();
            try {
                sequenceService.createSequence(name, description, ipAddress, port);
                showSequences();
                clear();
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                Alarm.showError("Error in creating new sequence.\n" + e.getMessage());
            }
        } else {
            Alarm.showError("Invalid input in fields.");
        }
    }

    @FXML
    void deleteSequence(ActionEvent event) {
        if(SelectedSequence.getSelectedSequence() != null) {
            if(Alarm.confirmationDelete(SelectedSequence.getSelectedSequence().getId(), SelectedSequence.getSelectedSequence().getName())) {
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
            try {
                sequenceService.updateSequence(SelectedSequence.getSelectedSequence().getId(), tfName.getText().trim(),
                        tfDescription.getText(), tfIPAddress.getText(), tfPort.getText());
                showSequences();
                clear();
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                Alarm.showError("Error in updating sequence.\n" + e.getMessage());
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
            colIPAddress.setCellValueFactory(new PropertyValueFactory<Sequence, String>("ipAddress"));
            colPort.setCellValueFactory(new PropertyValueFactory<Sequence, String>("port"));
        } catch (RuntimeException e) {
            Alarm.showError("An error occurred while loading sequences: " + e.getMessage());
        }

    }

    private void clear() {
        tfName.setText("");
        tfDescription.setText("");
        tfIPAddress.setText("");
        tfPort.setText("");
        SelectedSequence.setSelectedSequence(null);
        btnSave.setDisable(false);
    }


    private boolean validateTextFields() {
        return !tfName.getText().isBlank() && !tfDescription.getText().isBlank();
    }

    @FXML
    void switchToOP(ActionEvent event) {
        if(SelectedSequence.getSelectedSequence() != null) {
            try {
                App.switchToOPScreen();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                Alarm.showError("Error in switching to OP Screen.");
            }
        }

    }




}
