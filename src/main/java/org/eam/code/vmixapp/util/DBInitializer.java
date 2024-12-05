package org.eam.code.vmixapp.util;

import org.eam.code.vmixapp.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {


    public static void initializeDatabase() {
        Connection con = DBConnection.getCon();

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS sequences (Id INTEGER PRIMARY KEY, Name TEXT, Description Text)");
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
