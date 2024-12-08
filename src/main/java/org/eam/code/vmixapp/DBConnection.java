package org.eam.code.vmixapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    static String url = "jdbc:sqlite:vmdb.db";

    public static Connection getCon() {
        Connection con;
        try {
            con = DriverManager.getConnection(url);
            try (Statement stmt = con.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }
}
