package com.dataflow.components;

import com.model.Column;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class SqlServerSource implements SourceInterface{
    private String refId;
    private String classId;
    private String connectionManagerRefId;
    private String tableName;
    private List<Column> outputColumns;

    public SqlServerSource() {
        this.refId = "DataFlow.SqlServerSource";
        this.classId = "SqlServerSource";
    }

    public SqlServerSource(String connectionManagerRefId, String tableName) {
        this();
        this.tableName = tableName;
        this.connectionManagerRefId = connectionManagerRefId;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public List<Column> getOutputColumns() {
        return outputColumns;
    }

    @XmlElementWrapper(name = "outputs")
    @XmlElement(name = "outputColumn")
    public void setOutputColumns(List<Column> outputColumns) {
        this.outputColumns = outputColumns;
    }
}
