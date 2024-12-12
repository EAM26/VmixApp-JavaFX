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
    private final SequenceDAO sequenceDAO;
    Connection connection = null;
    PreparedStatement pstmt = null;
    ResultSet resultSet = null;

    public MyCameraDAO() {
        this.sequenceDAO = new SequenceDAO();
    }

    public List<MyCamera> getCamerasBySeqId(int seqId) {
        List<MyCamera> myCameraList = new ArrayList<>();
        String selectMessage = "SELECT * FROM cameras WHERE SeqId = ?";
        connection = DBConnection.getCon();

        try {
            pstmt = connection.prepareStatement(selectMessage);
            pstmt.setInt(1, seqId);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
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

    public void updateCam(String ref, String name, int id) {
        String updateMessage = "update cameras set Ref=?, Name=? where Id=?";
        connection = DBConnection.getCon();

        try {
            pstmt = connection.prepareStatement(updateMessage);
            pstmt.setString(1, ref);
            pstmt.setString(2, name);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCam(int id) {
        String deleteMessage = "delete from cameras where id = ?";
        connection = DBConnection.getCon();
        try {
            pstmt = connection.prepareStatement(deleteMessage);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MyCamera getCameraById(int id) {
        String getMessage = "Select * from cameras where id = ?";
        connection = DBConnection.getCon();

        try {
            pstmt = connection.prepareStatement(getMessage);
            pstmt.setInt(1, id);
            resultSet = pstmt.executeQuery();

            return prepareCamera(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MyCamera getCameraByRef(String ref) {
        String getMessage = "Select * from cameras where Ref = ? and SeqId=?";
        connection = DBConnection.getCon();

        try {
            pstmt = connection.prepareStatement(getMessage);
            pstmt.setString(1, ref);
            pstmt.setInt(2, SelectedSequence.getSelectedSequence().getId());
            resultSet = pstmt.executeQuery();

            return prepareCamera(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private MyCamera prepareCamera(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                MyCamera camera = new MyCamera();
                camera.setId(resultSet.getInt("Id"));
                camera.setRef(resultSet.getString("Ref"));
                camera.setName(resultSet.getString("Name"));
                int seqId = resultSet.getInt("SeqId");
                camera.setSequence(sequenceDAO.getSequenceById(seqId));
                return camera;
            } else {
    //                todo create own exception
                throw new RuntimeException("No camera found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
