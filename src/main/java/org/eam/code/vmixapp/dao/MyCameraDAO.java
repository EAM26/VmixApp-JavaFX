package org.eam.code.vmixapp.dao;

import org.eam.code.vmixapp.DBConnection;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.util.SelectedSequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyCameraDAO {
    Connection connection = null;
    PreparedStatement pstmt = null;
    ResultSet resultSet = null;

    public List<MyCamera> getCameras() {
        List<MyCamera> myCameraList = new ArrayList<>();

        if(SelectedSequence.getSelectedSequence() != null) {
            String selectMessage = "SELECT * FROM cameras WHERE SeqId = ?";
            Connection connection = DBConnection.getCon();

            try {;
                PreparedStatement pstmt = connection.prepareStatement(selectMessage);
                pstmt.setInt(1, SelectedSequence.getSelectedSequence().getId());
                ResultSet resultSet = pstmt.executeQuery();
                while (resultSet.next()){
                    MyCamera myCamera = new MyCamera();
                    myCamera.setId(resultSet.getInt("Id"));
                    myCamera.setName(resultSet.getString("Name"));
                    myCamera.setRef(resultSet.getString("Ref"));
                    Sequence sequence = SelectedSequence.getSelectedSequence();
                    myCamera.setSequence(sequence);
                    myCameraList.add(myCamera);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return myCameraList;
    }

    public void createCamera(String ref, String name, Sequence sequence) {
        String insertMessage = "INSERT INTO cameras (Ref, Name, SeqId) values(?, ?, ?)";

        connection = DBConnection.getCon();

        try {
            pstmt = connection.prepareStatement(insertMessage);
            pstmt.setString(1, ref);
            pstmt.setString(2, name);
            pstmt.setInt(3, sequence.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
