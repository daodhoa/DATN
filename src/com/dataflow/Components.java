package com.dataflow;

import com.dataflow.components.*;

public class Components {
    private ExcelSourceComponent excelSourceComponent;
    private SqlServerSource sqlServerSource;
    private DerivedColumn derivedColumn;
    private SqlServerDestination sqlServerDestination;
    private ExcelDestination excelDestination;
    private MySqlSource mySqlSource;
    private MysqlDestination mysqlDestination;

    public Components() {
        this.mySqlSource = null;
        this.excelSourceComponent = null;
        this.sqlServerSource = null;
        this.derivedColumn = null;
        this.sqlServerDestination = null;
        this.excelDestination = null;
        this.mysqlDestination = null;
    }

    public ExcelSourceComponent getExcelSourceComponent() {
        return excelSourceComponent;
    }

    public void setExcelSourceComponent(ExcelSourceComponent excelSourceComponent) {
        this.excelSourceComponent = excelSourceComponent;
    }

    public SqlServerSource getSqlServerSource() {
        return sqlServerSource;
    }

    public void setSqlServerSource(SqlServerSource sqlServerSource) {
        this.sqlServerSource = sqlServerSource;
    }

    public SourceInterface getSource() {
        if (this.excelSourceComponent != null) {
            return excelSourceComponent;
        }
        if (this.sqlServerSource != null) {
            return sqlServerSource;
        }
        if (this.mySqlSource != null) {
            return mySqlSource;
        }
        return null;
    }

    public MySqlSource getMySqlSource() {
        return mySqlSource;
    }

    public void setMySqlSource(MySqlSource mySqlSource) {
        this.mySqlSource = mySqlSource;
    }

    public DerivedColumn getDerivedColumn() {
        return derivedColumn;
    }

    public void setDerivedColumn(DerivedColumn derivedColumn) {
        this.derivedColumn = derivedColumn;
    }

    public SqlServerDestination getSqlServerDestination() {
        return sqlServerDestination;
    }

    public void setSqlServerDestination(SqlServerDestination sqlServerDestination) {
        this.sqlServerDestination = sqlServerDestination;
    }

    public ExcelDestination getExcelDestination() {
        return excelDestination;
    }

    public void setExcelDestination(ExcelDestination excelDestination) {
        this.excelDestination = excelDestination;
    }

    public DestinationInterface getDestination() {
        if (this.excelDestination != null) {
            return this.excelDestination;
        }
        if (this.sqlServerDestination != null) {
            return this.sqlServerDestination;
        }
        if (this.mysqlDestination != null) {
            return this.mysqlDestination;
        }
        return null;
    }

    public MysqlDestination getMysqlDestination() {
        return mysqlDestination;
    }

    public void setMysqlDestination(MysqlDestination mysqlDestination) {
        this.mysqlDestination = mysqlDestination;
    }
}
