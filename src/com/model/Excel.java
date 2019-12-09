package com.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name = "excelTemp")
public class Excel implements Connection{
    private String filePath;
    private List<Column> listColumns;
    private boolean isFirstRow;
    private int sheetIndex;

    public Excel(){
    };

    public Excel(String filePath) {
        this.filePath = filePath;
    }

    public Excel(String filePath, List<Column> listColumns, boolean isFirstRow, int sheetIndex) {
        this.filePath = filePath;
        this.listColumns = listColumns;
        this.isFirstRow = isFirstRow;
        this.sheetIndex = sheetIndex;
    }

    public String getFilePath() {
        return filePath;
    }

    @XmlAttribute(name = "path")
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }
    @XmlElement(name = "sheetIndex")
    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public boolean isFirstRow() {
        return isFirstRow;
    }

    @XmlAttribute(name = "isFirstRow")
    public void setFirstRow(boolean firstRow) {
        isFirstRow = firstRow;
    }

    public List<Column> getListColumns() {
        return listColumns;
    }

    @XmlElementWrapper(name = "excelColumns")
    @XmlElement(name = "excelColumn")
    public void setListColumns(List<Column> listColumns) {
        this.listColumns = listColumns;
    }

    @Override
    public String toString() {
        return "Excel [path =" + filePath + "]" + " Columns: " + listColumns.size();
    }
}
