package org.eam.code.vmixapp;

import org.eam.code.vmixapp.util.Alarm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:sqlite:vmdb.db";
    private static Connection connection;

    private DBConnection() {}

    public static synchronized Connection getCon() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);

                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Alarm.showError("Error in connecting to database.");
        }
        return connection;
    }

//    public static synchronized void closeConnection() {
//        try {
//            if (connection != null && !connection.isClosed()) {
//                connection.close();
//                System.out.println("Connection Closed");
//            }
//        } catch (SQLException e) {
//            System.err.println("Error Closing Database Connection");
//            e.printStackTrace();
//        } finally {
//            connection = null;
//        }
//    }
}



