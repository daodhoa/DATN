package com.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MysqlConnection extends DBConnection {

    public MysqlConnection(String hostName, String userName, String password) {
        super(hostName, userName, password);
    }

    @Override
    public List<String> getListDatabase() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn;
        String connectionString =  "jdbc:mysql://" + this.getHostName() + ":3306/" + this.getDatabaseName();
        System.out.println(connectionString);
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(connectionString, this.getUserName(), this.getPassword());
        System.out.println("connect successfully!");
        return conn;
    }
}
