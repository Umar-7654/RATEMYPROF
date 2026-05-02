package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/ratemyausprof";
    private static final String USER = "root";
    private static final String PASSWORD = "NNFy@123123.";
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } 
        catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found. Make sure the connector JAR is added to Eclipse and Tomcat lib.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}