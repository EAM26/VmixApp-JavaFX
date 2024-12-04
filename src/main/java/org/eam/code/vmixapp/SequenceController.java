package org.eam.code.vmixapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SequenceController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableView<Sequence> table;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfName;

    @FXML
    void clearField(ActionEvent event) {

    }

    @FXML
    void createSequence(ActionEvent event) {

    }

    @FXML
    void deleteSequence(ActionEvent event) {

    }

    @FXML
    void updateSequence(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
