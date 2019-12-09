package com.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name = "SqlServer")
public class SqlServer implements Connection {
    private String hostname;
    private String username;
    private String password;
    private String database;
    private List<Column> listColumns;
    private String tableName;

    public SqlServer(){}

    public SqlServer(String hostname, String username, String password, String database) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public String getHostname() {
        return hostname;
    }

    @XmlAttribute(name = "hostname")
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUsername() {
        return username;
    }

    @XmlAttribute(name = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @XmlAttribute(name = "password")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    @XmlAttribute(name = "database")
    public void setDatabase(String database) {
        this.database = database;
    }

    public List<Column> getListColumns() {
        return listColumns;
    }

    public String getTableName() {
        return tableName;
    }
    @XmlElement(name = "tableName")
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public void setListColumns(List<Column> listColumns) {
        this.listColumns = listColumns;
    }
}
