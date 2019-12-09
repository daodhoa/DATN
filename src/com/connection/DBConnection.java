package com.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class DBConnection {
    protected String hostName;
    protected String userName;
    protected String password;
    protected String databaseName;
    protected String tableName;

    public DBConnection(String hostName, String userName, String password) {
        this.hostName = hostName;
        this.userName = userName;
        this.password = password;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public abstract List<String> getListDatabase() throws SQLException;

    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}
