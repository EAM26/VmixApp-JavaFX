package org.eam.code.vmixapp.dao;

import org.eam.code.vmixapp.DBConnection;
import org.eam.code.vmixapp.model.MyCamera;
import org.eam.code.vmixapp.model.Scene;
import org.eam.code.vmixapp.model.Sequence;
import org.eam.code.vmixapp.service.SequenceService;
import org.eam.code.vmixapp.service.MyCameraService;
import org.eam.code.vmixapp.util.SelectedSequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SceneDao {
    private final SequenceService sequenceService;
    private final MyCameraService myCameraService;

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet resultSet = null;

    public SceneDao() {
        this.sequenceService = new SequenceService();
        this.myCameraService = new MyCameraService();
    }

    public List<Scene> getScenes() {
        List<Scene> sceneList = new ArrayList<>();
        String selectMessage = "select * from scenes where SeqId=?";

        con = DBConnection.getCon();
        try {
            pstmt = con.prepareStatement(selectMessage);
            pstmt.setInt(1, SelectedSequence.getSelectedSequence().getId());
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Scene scene = new Scene();
                scene.setId(resultSet.getInt("Id"));
                scene.setNumber(resultSet.getInt("Number"));
                scene.setName(resultSet.getString("Name"));
                scene.setDescription(resultSet.getString("Description"));
                Sequence sequence = sequenceService.getSequenceById(resultSet.getInt("SeqId"));
                scene.setSequence(sequence);
                MyCamera camera = myCameraService.getCameraById(resultSet.getInt("CamId"));
                scene.setCamera(camera);
                sceneList.add(scene);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sceneList;
    }
}
