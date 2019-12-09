package com.services;

import com.model.Column;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlService extends SqlService {

    public MysqlService(Connection connection) {
        super(connection);
    }

    @Override
    public List<String> getListTableNames(String database) throws SQLException {
        List<String> listTableNames = new ArrayList<>();
        String sqlString = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
        preparedStatement.setString(1, database);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            listTableNames.add(rs.getString(1));
        }
        return listTableNames;
    }

    public Map<String, Integer> getListColumns(String tableName, String database) throws SQLException {
        Map<String, Integer> listColumns = new HashMap<>();
        String sqlString = "Select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ? and TABLE_SCHEMA = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlString);
        preparedStatement.setString(1, tableName);
        preparedStatement.setString(2, database);
        ResultSet rs = preparedStatement.executeQuery();
        int index = 0;
        while (rs.next()) {
            listColumns.put(rs.getString(1), index);
            index ++;
        }
        return listColumns;
    }

    public Map<String, Column> getMapColumns(String tableName, String database) throws SQLException {
        Map<String, Column> listOutputColumns = new HashMap<>();
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
                columnLength = Integer.valueOf((Integer) length);
            } catch (Exception e) {
                columnLength = 100;
                System.out.println("Error when cast to int");
            }
            column.setLength(columnLength);
            listOutputColumns.put(column.getName(), column);
        }

        return listOutputColumns;
    }
}
