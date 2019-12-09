package com.dataflow;
import com.model.Excel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ExcelManager {
    @XmlAttribute
    private String refId;
    @XmlAttribute
    private String creationName;
    @XmlAttribute
    private String objectName;

    private Excel excel;
    public ExcelManager(){}
    public ExcelManager(Excel excel) {
        this.excel = excel;
        this.objectName = "EXCEL";
        this.creationName = "EXCEL";
        this.refId = "connectionManagers[" + objectName + "]";
    }

    public Excel getExcel() {
        return this.excel;
    }

    @XmlElement(name = "connection")
    public void setExcel(Excel excel) {
        this.excel = excel;
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
}
