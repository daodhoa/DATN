package com.dataflow.components;

import com.model.Column;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

public class DerivedColumn {
    private String refId;
    private String classId;
    private List<Column> inputColumns;
    private List<Column> outputColumns;

    public DerivedColumn() {
        this.refId = "DataFlow.DerivedColumn";
        this.classId = "DerivedColumn";
        inputColumns = new ArrayList<>();
        outputColumns = new ArrayList<>();
    }

    public List<Column> getInputColumns() {
        return inputColumns;
    }

    @XmlElementWrapper(name = "inputColumns")
    @XmlElement(name = "inputColumn")
    public void setInputColumns(List<Column> inputColumns) {
        this.inputColumns = inputColumns;
    }

    public List<Column> getOutputColumns() {
        return outputColumns;
    }

    @XmlElementWrapper(name = "outputColumns")
    @XmlElement(name = "outputColumn")
    public void setOutputColumns(List<Column> outputColumns) {
        this.outputColumns = outputColumns;
    }
}
