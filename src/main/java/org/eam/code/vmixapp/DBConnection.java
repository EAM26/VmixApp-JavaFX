package org.eam.code.vmixapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
//    static String user = "root";
//    static String password = "password";
//    static String url = "jdbc:mysql://localhost:3306/vmix";
    static String url = "jdbc:sqlite:vmdb.db";
//    static String driver = "com.mysql.cj.jdbc.Driver";

//    public static Connection getCon() {
//        Connection con = null;
//        try {
//            Class.forName(driver);
//            try {
//                con = DriverManager.getConnection(url, user, password);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        return con;
//    }

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
