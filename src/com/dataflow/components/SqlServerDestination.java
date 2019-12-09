package com.dataflow.components;

import com.model.Column;

import java.util.List;

public class SqlServerDestination implements DestinationInterface{
    private String refId;
    private String classId;
    private String tableName;
    private String connectionManagerRefId;
    private List<Column> inputColumns;

    public SqlServerDestination() {
        this.refId = "DataFlow.SqlServerDestination";
        this.classId = "SqlServerDestination";
    }

    public String getRefId() {
        return refId;
    }

    public String getClassId() {
        return classId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getConnectionManagerRefId() {
        return connectionManagerRefId;
    }

    public void setConnectionManagerRefId(String connectionManagerRefId) {
        this.connectionManagerRefId = connectionManagerRefId;
    }

    public List<Column> getInputColumns() {
        return inputColumns;
    }

    public void setInputColumns(List<Column> inputColumns) {
        this.inputColumns = inputColumns;
    }
}
