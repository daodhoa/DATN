package com.dataflow;

import com.model.SqlServer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SqlServerManager {
    @XmlAttribute
    private String refId;
    @XmlAttribute
    private String creationName;
    @XmlAttribute
    private String objectName;

    private SqlServer sqlServer;

    public SqlServerManager(){}
    public SqlServerManager(SqlServer sqlServer) {
        this.sqlServer = sqlServer;
        this.objectName = sqlServer.getHostname() + "." + sqlServer.getDatabase();
        this.creationName = "SQL_SERVER";
        this.refId = "connectionManagers[" + objectName + "]";
    }

    public String getRefId() {
        return refId;
    }

    public String getCreationName() {
        return creationName;
    }

    public String getObjectName() {
        return objectName;
    }

    public SqlServer getSqlServer() {
        return this.sqlServer;
    }

    @XmlElement(name = "connection")
    public void setSqlServer(SqlServer sqlServer) {
        this.sqlServer = sqlServer;
    }
}
