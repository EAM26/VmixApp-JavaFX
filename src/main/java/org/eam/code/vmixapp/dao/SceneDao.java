package org.eam.code.vmixapp.dao;

import org.eam.code.vmixapp.DBConnection;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Scene;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.util.SelectedSequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SceneDao {
    private final SequenceDAO sequenceDAO;
    private final MyCameraDAO cameraDAO;

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet resultSet = null;

    public SceneDao() {
        this.sequenceDAO = new SequenceDAO();
        this.cameraDAO = new MyCameraDAO();
    }

    public List<Scene> getScenesBySeqId(int seqId) {
        List<Scene> sceneList = new ArrayList<>();
        String selectMessage = "select * from scenes where SeqId=?";
        con = DBConnection.getCon();

        try {
            pstmt = con.prepareStatement(selectMessage);
            pstmt.setInt(1, seqId);
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Scene scene = new Scene();
                scene.setId(resultSet.getInt("Id"));
                scene.setNumber(resultSet.getInt("Number"));
                scene.setName(resultSet.getString("Name"));
                scene.setDescription(resultSet.getString("Description"));
                scene.setSequence(sequenceDAO.getSequenceById(resultSet.getInt("SeqId")));
                scene.setCamera(cameraDAO.getCameraById(resultSet.getInt("CamId")));
                sceneList.add(scene);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sceneList;
    }

    public void createScene(int sceneNumber, String sceneName, String sceneDescription, MyCamera camera, Sequence sequence) {
        String createMessage = "insert into scenes (Number, Name, Description, CamId, SeqId) values (?, ?, ?, ?, ?)";
        con = DBConnection.getCon();

        try {
            pstmt = con.prepareStatement(createMessage);
            pstmt.setInt(1, sceneNumber);
            pstmt.setString(2, sceneName);
            pstmt.setString(3, sceneDescription);
            pstmt.setInt(4, camera.getId());
            pstmt.setInt(5, sequence.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteScene(int id) {
        String deleteMessage = "delete from scenes where Id=?";
        con = DBConnection.getCon();
        try {
            pstmt = con.prepareStatement(deleteMessage);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
