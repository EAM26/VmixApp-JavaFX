package org.eam.code.vmixapp.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eam.code.vmixapp.DBConnection;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Sequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SequenceDAO {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet resultSet = null;

    public List<Sequence> getSequences() {
        List<Sequence> sequences = new ArrayList<>();

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
}
