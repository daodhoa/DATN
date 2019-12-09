package com.dataflow;

import com.model.MySql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class MySqlManager {
    @XmlAttribute
    private String refId;
    @XmlAttribute
    private String creationName;
    @XmlAttribute
    private String objectName;

    private MySql mySql;

    public MySqlManager() {}
    public MySqlManager(MySql mySql) {
        this.mySql = mySql;
        this.objectName = mySql.getHostname() + "." + mySql.getDatabase();
        this.creationName = "MYSQL";
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

    public MySql getMySql() {
        return this.mySql;
    }

    @XmlElement(name = "connection")
    public void setMySql(MySql sqlServer) {
        this.mySql = sqlServer;
    }
}
