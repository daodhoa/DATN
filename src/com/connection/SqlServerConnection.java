package com.connection;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@XmlRootElement(name = "Sql Server")
public class SqlServerConnection extends DBConnection {

    public SqlServerConnection(String hostName, String userName, String password){
        super(hostName, userName, password);
    }

    @Override
    public List<String> getListDatabase() throws SQLException {
        List<String> listDatabases = new ArrayList<>();
        Connection conn = null;

        String dbURL = "jdbc:sqlserver://" + this.getHostName() + "; integratedSecurity=true";
        System.out.println(dbURL);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(dbURL, this.getUserName(), this.getPassword());
            System.out.println("connect successfully!");

            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT name FROM master.dbo.sysdatabases");
            while (rs.next()) {
                listDatabases.add(rs.getString(1));
            }

            conn.close();
        } catch (Exception ex) {
            System.out.println("connect failure!");
            ex.printStackTrace();
        }
        return listDatabases;
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn;
        String connectionString = "jdbc:sqlserver://" + this.getHostName() + "; databaseName="+
                this.getDatabaseName() + "; integratedSecurity=true";
        System.out.println(connectionString);
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn = DriverManager.getConnection(connectionString, this.getUserName(), this.getPassword());
        System.out.println("connect successfully!");
        return conn;
    }
}
