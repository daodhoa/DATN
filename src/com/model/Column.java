package com.model;

import com.enums.DataType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "column")
public class Column {
    private String id;
    private String name;
    private DataType dataType;
    private int length;
    private String linearId;
    private String expression;

    public Column() {
        this.id = null;
        this.linearId = null;
        this.expression = null;
        this.name = null;
        this.dataType = DataType.STRING;
        this.length = 0;
    }

    public Column(String name, DataType dataType, int length) {
        this.name = name;
        this.dataType = dataType;
        this.length = length;
        this.id = null;
        this.linearId = null;
        this.expression = null;
    }

    public Column(String id, String name, DataType dataType, int length) {
        this.id = id;
        this.name = name;
        this.dataType = dataType;
        this.length = length;
        this.linearId = null;
        this.expression = null;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @XmlAttribute
    public void setLinearId(String linearId) {
        this.linearId = linearId;
    }

    public String getLinearId() {
        return linearId;
    }

    public String getExpression() {
        return expression;
    }

    @XmlElement
    public void setExpression(String expression) {
        this.expression = expression;
    }
}
