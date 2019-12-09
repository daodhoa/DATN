package com.dataflow.components;

import com.model.Column;

import java.util.List;

public class ExcelDestination implements DestinationInterface {
    private String refId;
    private String classId;
    private String connectionManagerRefId;
    private List<Column> inputColumns;

    public ExcelDestination() {
        this.refId = "DataFlow.ExcelDestination";
        this.classId = "ExcelDestination";
    }

    public String getRefId() {
        return refId;
    }

    public String getClassId() {
        return classId;
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
