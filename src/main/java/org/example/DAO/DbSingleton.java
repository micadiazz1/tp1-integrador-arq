package org.example.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbSingleton {
    private static final String url = "jdbc:mysql://localhost:3308/db_arquitectura";
    private static final String username = "root";
    private static final String password = "";
    private static Connection conn;

    public static Connection getInstance() throws SQLException {
        if (conn == null){
            conn = DriverManager.getConnection(url, username, password);
        }
        return conn;
    }
}
