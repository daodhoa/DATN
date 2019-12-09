package com.dataflow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "connectionManagers")
public class ConnectionManager {
    private ExcelManager excelManager;
    private SqlServerManager sqlServerManager;
    private SqlServerManager sqlServerManagerDestination;
    private ExcelManager excelManagerDestination;
    private MySqlManager mySqlManager;
    private MySqlManager mySqlManagerDestination;

    public ConnectionManager() {
        this.mySqlManager = null;
        this.excelManager = null;
        this.sqlServerManager = null;
        this.sqlServerManagerDestination = null;
        this.excelManagerDestination = null;
        this.mySqlManagerDestination = null;
    }

    public ExcelManager getExcelManager() {
        return excelManager;
    }

    @XmlElement
    public void setExcelManager(ExcelManager excelManager) {
        this.excelManager = excelManager;
    }

    public SqlServerManager getSqlServerManager() {
        return sqlServerManager;
    }

    @XmlElement
    public void setSqlServerManager(SqlServerManager sqlServerManager) {
        this.sqlServerManager = sqlServerManager;
    }

    public SqlServerManager getSqlServerManagerDestination() {
        return sqlServerManagerDestination;
    }
    @XmlElement
    public void setSqlServerManagerDestination(SqlServerManager sqlServerManagerDestination) {
        this.sqlServerManagerDestination = sqlServerManagerDestination;
    }
    public ExcelManager getExcelManagerDestination() {
        return excelManagerDestination;
    }
    @XmlElement
    public void setExcelManagerDestination(ExcelManager excelManagerDestination) {
        this.excelManagerDestination = excelManagerDestination;
    }
    public MySqlManager getMySqlManager() {
        return mySqlManager;
    }
    @XmlElement
    public void setMySqlManager(MySqlManager mySqlManager) {
        this.mySqlManager = mySqlManager;
    }

    public MySqlManager getMySqlManagerDestination() {
        return mySqlManagerDestination;
    }
    @XmlElement
    public void setMySqlManagerDestination(MySqlManager mySqlManagerDestination) {
        this.mySqlManagerDestination = mySqlManagerDestination;
    }

    public String getRefIdOfDestination() {
        if (this.excelManagerDestination != null) {
            return "EXCEL: \n" + excelManagerDestination.getRefId() + "\n File path: "
                    + excelManagerDestination.getExcel().getFilePath()
                    + "\n Sheet Index: " + excelManagerDestination.getExcel().getSheetIndex();
        }
        if (this.sqlServerManagerDestination != null) {
            return "SQL SERVER: \n" + sqlServerManagerDestination.getRefId();
        }
        if (this.mySqlManagerDestination != null) {
            return "MYSQL: \n" + mySqlManagerDestination.getRefId();
        }
        return "";
    }

    public String getRefIdOfSource() {
        if (this.excelManager != null) {
            return  "EXCEL: \n" + excelManager.getRefId() + "\nFile path: "
                    + excelManager.getExcel().getFilePath()
                    + "\nSheet Index: " + excelManager.getExcel().getSheetIndex();
        }
        if (this.sqlServerManager != null) {
            return "SQL SERVER: \n" + sqlServerManager.getRefId();
        }
        if (this.mySqlManager != null) {
            return "MYSQL: \n" + mySqlManager.getRefId();
        }
        return "";
    }
}
