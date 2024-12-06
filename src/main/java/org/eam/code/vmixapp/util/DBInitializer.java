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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS sequences (Id INTEGER PRIMARY KEY, Name TEXT, " +
                    "Description Text)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cameras (Id INTEGER PRIMARY KEY, Ref TEXT,  " +
                    "Name Text, SeqId INTEGER, FOREIGN KEY(SeqId) REFERENCES sequences(Id))");
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
