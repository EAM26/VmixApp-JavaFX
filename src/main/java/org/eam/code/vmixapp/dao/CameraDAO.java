package org.eam.code.vmixapp.dao;

import org.eam.code.vmixapp.DBConnection;
import org.eam.code.vmixapp.model.Camera;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.util.SelectedSequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CameraDAO {

    public List<Camera> getCameras() {
        List<Camera> cameraList = new ArrayList<>();

        if(SelectedSequence.getSelectedSequence() != null) {
            String selectMessage = "SELECT * FROM cameras WHERE Id = ?";
            Connection connection = DBConnection.getCon();

            try {;
                PreparedStatement pstmt = connection.prepareStatement(selectMessage);
                pstmt.setInt(1, SelectedSequence.getSelectedSequence().getId());
                ResultSet resultSet = pstmt.executeQuery();
                while (resultSet.next()){
                    Camera camera = new Camera();
                    camera.setId(resultSet.getInt("Id"));
                    camera.setName(resultSet.getString("Name"));
                    camera.setNumber(resultSet.getInt("Number"));
                    Sequence sequence = SelectedSequence.getSelectedSequence();
                    camera.setSequence(sequence);
                    cameraList.add(camera);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return cameraList;
    }
}
