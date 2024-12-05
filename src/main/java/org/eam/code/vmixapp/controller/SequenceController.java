package org.eam.code.vmixapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.eam.code.vmixapp.DBConnection;
import org.eam.code.vmixapp.model.Sequence;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SequenceController implements Initializable {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet resultSet = null;

    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        showSequences();
    }

    public ObservableList<Sequence> getSequences() {
        ObservableList<Sequence> sequences = FXCollections.observableArrayList();

        String insert = "select * from sequences";

        con = DBConnection.getCon();
        try {
            pstmt = con.prepareStatement(insert);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Sequence sequence = new Sequence();
                sequence.setId(resultSet.getInt("Id"));
                sequence.setName(resultSet.getString("Name"));
                sequence.setDescription(resultSet.getString("Description"));

                sequences.add(sequence);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sequences;
    }

    public void showSequences() {
        ObservableList<Sequence> seqList = getSequences();
        table.setItems(seqList);
        colId.setCellValueFactory(new PropertyValueFactory<Sequence, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Sequence, String>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Sequence, String>("description"));
    }

    @FXML
    void getSelectedData(MouseEvent event) {
        Sequence selectedSeq = table.getSelectionModel().getSelectedItem();
        if (selectedSeq != null) {
            setId(selectedSeq.getId());
            tfName.setText(selectedSeq.getName());
            tfDescription.setText(selectedSeq.getDescription());
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

            String insertMessage = "insert into sequences(Name, Description) values(?, ?)";
            con = DBConnection.getCon();
            try {
                pstmt = con.prepareStatement(insertMessage);
                pstmt.setString(1, tfName.getText());
                pstmt.setString(2, tfDescription.getText());
                pstmt.executeUpdate();
                showSequences();
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void deleteSequence(ActionEvent event) {
        if(id != 0) {
            if(alarmDelete()) {
                String deleteMessage = "delete from sequences where id=?";
                con = DBConnection.getCon();
                try {
                    pstmt = con.prepareStatement(deleteMessage);
                    pstmt.setInt(1, getId());
                    pstmt.executeUpdate();
                    showSequences();
                    clear();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            clear();
        }
    }

    @FXML
    void updateSequence(ActionEvent event) {
        if(getId() != 0 && validateTextFields()) {
            String updateMessage = "update sequences set Name=?, Description=? where id=?";
            con = DBConnection.getCon();
            try {
                pstmt = con.prepareStatement(updateMessage);
                pstmt.setString(1, tfName.getText());
                pstmt.setString(2, tfDescription.getText());
                pstmt.setInt(3, getId());
                pstmt.executeUpdate();
                showSequences();
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void clear() {
        tfName.setText("");
        tfDescription.setText("");
        setId(0);
        btnSave.setDisable(false);
    }

    public boolean alarmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete sequence with id: " + id + "  ?");
        alert.setContentText("This action cannot be undone.");

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    public boolean validateTextFields() {
        return !tfName.getText().isBlank() && !tfDescription.getText().isBlank();
    }

}
