package org.eam.code.vmixapp.dao;
import org.eam.code.vmixapp.DBConnection;
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

        String selectMessage = "select * from sequences";

        con = DBConnection.getCon();
        try {
            pstmt = con.prepareStatement(selectMessage);
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

    public Sequence getSequenceById(int id) {
        String getMessage = "Select * from sequences where id=?";
        con = DBConnection.getCon();

        try {
            pstmt = con.prepareStatement(getMessage);
            pstmt.setInt(1, id);
            resultSet = pstmt.executeQuery();
            if(resultSet.next()) {
                Sequence sequence = new Sequence();
                sequence.setId(id);
                sequence.setName(resultSet.getString("Name"));
                sequence.setDescription(resultSet.getString("Description"));
                return sequence;
            } else {
//                todo: create own not found exception
                throw new RuntimeException();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSequence(String name, String description) {
        String createMessage = "insert into sequences (Name, Description) values (?, ?)";
        con = DBConnection.getCon();

        try {
            PreparedStatement pstmt = con.prepareStatement(createMessage);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSequence(int id, String name, String description) {
        String updateMessage = "update sequences set Name=?, Description=? where Id=?";
        con = DBConnection.getCon();

        try {
            PreparedStatement pstmt = con.prepareStatement(updateMessage);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteSequence(int id) {
        String deleteMessage = "delete from sequences where id = ?";

        con = DBConnection.getCon();
        try {
            PreparedStatement pstmt = con.prepareStatement(deleteMessage);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
