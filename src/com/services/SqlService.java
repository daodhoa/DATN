package com.services;

import com.model.Column;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class SqlService {
    protected Connection connection;

    public SqlService(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    abstract List<String> getListTableNames(String database) throws SQLException;

    public List<Column> getListOutputColumns(String tableName, String database) throws SQLException {
        List<Column> listOutputColumns = new ArrayList<>();
        String sqlString = "Select COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH " +
                "from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ? and TABLE_SCHEMA = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlString);
        preparedStatement.setString(1, tableName);
        preparedStatement.setString(2, database);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Column column = new Column();
            column.setName(rs.getString(1));
            column.setDataType(DataTypeConversion.database2Java(rs.getString(2)));
            Object length = rs.getObject(3);
            int columnLength;
            try {
                columnLength = (int) length;
            } catch (Exception e) {
                columnLength = 100;
                System.out.println("Error when cast to int");
            }
            column.setLength(columnLength);
            listOutputColumns.add(column);
        }

        return listOutputColumns;
    }

    public List<List<String>> getDataFromTable(String tableName) throws SQLException {
        List<List<String>> listDataFromTables = new ArrayList<>();
        String sqlString = "Select * from " + tableName;
        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlString);
        ResultSet rs = preparedStatement.executeQuery();
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int numberOfColumn = resultSetMetaData.getColumnCount();
        while (rs.next()) {
            List<String> listOneRow = new ArrayList<>();
            for (int i = 1; i <= numberOfColumn; i++) {
                listOneRow.add(String.valueOf(rs.getObject(i)));
            }
            listDataFromTables.add(listOneRow);
        }
        return listDataFromTables;
    }

    public boolean insertDataToTable(String sqlString, List<Column> listInputColumns, List<Map<String, String>> listInputDataToDestination) {
        listInputColumns.forEach(column -> {
            System.out.println(column.getId() + ": " + column.getName());
        });
        System.out.println(sqlString);
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sqlString);
            connection.setAutoCommit(false);
            try {
                for (int i = 0; i < listInputDataToDestination.size(); i++) {
                    Map<String, String> mapOneRow = listInputDataToDestination.get(i);
                    System.out.println("Khong thuc hien");
                    for (int j = 0; j < listInputColumns.size(); j ++) {
                        preparedStatement.setObject(j + 1, mapOneRow.get(listInputColumns.get(j).getLinearId()));
                    }

                    preparedStatement.addBatch();
                    if (i == 100 || i == listInputDataToDestination.size() - 1) {
                        System.out.println("Thuc hien query");
                        preparedStatement.executeBatch();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                connection.rollback();
                return false;
            }
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createTable(String sqlString) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    public List<String> getPrimaryKey(String tableName) throws SQLException {
        List<String> listPrimaryKeys = new ArrayList<>();
        String sql = "SELECT KU.table_name as TABLENAME,column_name as PRIMARYKEYCOLUMN\n" +
                "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS TC\n" +
                "INNER JOIN\n" +
                "    INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KU\n" +
                "          ON TC.CONSTRAINT_TYPE = 'PRIMARY KEY' AND\n" +
                "             TC.CONSTRAINT_NAME = KU.CONSTRAINT_NAME AND \n" +
                "             KU.table_name= " + tableName +
                "\nORDER BY KU.TABLE_NAME, KU.ORDINAL_POSITION";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            listPrimaryKeys.add(rs.getString(2));
        }
        return listPrimaryKeys;
    }
}
