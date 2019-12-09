package com.dataflow.components;

import com.model.Column;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class ExcelSourceComponent implements SourceInterface{
    private String refId;
    private String classId;
    private String connectionManagerRefId;
    private List<Column> outputColumns;

    public ExcelSourceComponent() {
        this.refId = "DataFlow.ExcelSource";
        this.classId = "ExcelSource";
    }

    public ExcelSourceComponent(String connectionManagerRefId) {
        this();
        this.connectionManagerRefId = connectionManagerRefId;
    }

    public void setConnectionManagerRefId(String connectionManagerRefId) {
        this.connectionManagerRefId = connectionManagerRefId;
    }
    @XmlElementWrapper(name = "outputs")
    @XmlElement(name = "outputColumn")
    public void setOutputColumns(List<Column> outputColumns) {
        this.outputColumns = outputColumns;
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

    @Override
    public List<Column> getOutputColumns() {
        return outputColumns;
    }
}
