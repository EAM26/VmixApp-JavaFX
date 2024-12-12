package org.eam.code.vmixapp.util;

import org.eam.code.vmixapp.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Validation {
    public static boolean existsInTable(String tableName, String attribute, int param) {
        String query = "SELECT EXISTS (SELECT 1 FROM " + tableName + " WHERE " + attribute + "=?)";
        try (Connection con = DBConnection.getCon();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, param);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence in " + tableName, e);
        }
        return false;
    }

    public static boolean existsInTable(String tableName, String attribute, String param) {
        String query = "SELECT EXISTS (SELECT 1 FROM " + tableName + " WHERE " + attribute + "=?)";
        try (Connection con = DBConnection.getCon();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, param);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence in " + tableName, e);
        }
        return false;
    }
}
