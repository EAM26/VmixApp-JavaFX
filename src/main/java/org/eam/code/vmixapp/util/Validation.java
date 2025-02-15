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

    public static boolean existsInTable(String tableName, String attr1, int param1, String attr2, int param2) {
        String query = "SELECT EXISTS (SELECT 1 FROM " + tableName + " WHERE " + attr1 + "=? AND " + attr2 + "=?)";
        try (Connection con = DBConnection.getCon();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, param1);
            pstmt.setInt(2, param2);
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

    public static boolean existsInTable(String tableName, String attr1, String param1, String attr2, int param2) {
        String query = "SELECT EXISTS (SELECT 1 FROM " + tableName + " WHERE " + attr1 + "=? AND " + attr2 + "=?)";
        try (Connection con = DBConnection.getCon();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, param1);
            pstmt.setInt(2, param2);
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
