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
                    myCamera.setNumber(resultSet.getInt("Number"));
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
}
