package com.dataflow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dataFlow")
public class DataFlow {
    private ConnectionManager connectionManager;
    private Executables executables;

    public DataFlow() {
        this.executables = null;
        this.connectionManager = null;
    }

    public DataFlow(ConnectionManager connectionManager, Executables executables) {
        this.connectionManager = connectionManager;
        this.executables = executables;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    @XmlElement(name = "connectionManagers")
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @XmlElement(name = "executables")
    public Executables getExecutables() {
        return executables;
    }

    public void setExecutables(Executables executables) {
        this.executables = executables;
    }

    public void refresh() {
        this.setExecutables(null);
        this.setConnectionManager(null);
    }
}
