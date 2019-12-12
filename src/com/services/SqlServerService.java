package com.services;
import com.model.Column;
import java.sql.*;
import java.util.*;

public class SqlServerService extends SqlService{

    public SqlServerService(Connection connection) {
        super(connection);
    }

    @Override
    List<String> getListTableNames(String database) throws SQLException {
        return null;
    }

    public List<Column> getListOutputColumns(String tableName) throws SQLException {
        String[] tableSchemaTableName = tableName.split("\\.");

        List<Column> listOutputColumns = new ArrayList<>();
        String sqlString = "Select COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH " +
                "from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ? AND TABLE_SCHEMA = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlString);
        preparedStatement.setString(1, tableSchemaTableName[1]);
        preparedStatement.setString(2, tableSchemaTableName[0]);
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

    public List<String> getListTableNames() throws SQLException {
        List<String> listTableNames = new ArrayList<>();
        String sqlString = "SELECT TABLE_SCHEMA, TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            listTableNames.add(rs.getString(1) + "." + rs.getString(2));
        }
        return listTableNames;
    }

    public Map<String, Integer> getListColumns(String tableName) throws SQLException {
        String tableSchemaTableName[] = tableName.split("\\.");
        Map<String, Integer> listColumns = new HashMap<>();
        String sqlString = "Select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ? and TABLE_SCHEMA = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlString);
        preparedStatement.setString(1, tableSchemaTableName[1]);
        preparedStatement.setString(2, tableSchemaTableName[0]);
        ResultSet rs = preparedStatement.executeQuery();
        int index = 0;
        while (rs.next()) {
            listColumns.put(rs.getString(1), index);
            index ++;
        }
        return listColumns;
    }

    public Map<String, Column> getMapColumns(String tableName) throws SQLException {
        String tableSchemaTableName[] = tableName.split("\\.");
        Map<String, Column> listOutputColumns = new HashMap<>();
        String sqlString = "Select COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH " +
                "from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ? AND TABLE_SCHEMA = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlString);
        preparedStatement.setString(1, tableSchemaTableName[1]);
        preparedStatement.setString(2, tableSchemaTableName[0]);
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
            listOutputColumns.put(column.getName(), column);
        }

        return listOutputColumns;
    }

    public boolean checkPrimaryKey(String tableName) throws SQLException {
        String[] tableSchemaTableName = tableName.split("\\.");

        String sqlString = "SELECT *  \n" +
                "FROM information_schema.table_constraints  \n" +
                "WHERE constraint_type = 'PRIMARY KEY' AND TABLE_NAME = ? AND CONSTRAINT_SCHEMA = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlString);
        preparedStatement.setString(1, tableSchemaTableName[1]);
        preparedStatement.setString(2, tableSchemaTableName[0]);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public List<String> getPrimaryKey(String tableName) throws SQLException {
        List<String> listKeys = new ArrayList<>();
        String[] tableSchemaTableName = tableName.split("\\.");
        String sqlString = "SELECT KU.table_name as TABLENAME,column_name as PRIMARYKEYCOLUMN \n" +
                "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS TC \n" +
                "INNER JOIN \n" +
                "    INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KU \n" +
                "          ON TC.CONSTRAINT_TYPE = 'PRIMARY KEY' AND \n" +
                "             TC.CONSTRAINT_NAME = KU.CONSTRAINT_NAME AND \n" +
                "             KU.table_name= ? \n" +
                "ORDER BY KU.TABLE_NAME, KU.ORDINAL_POSITION";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlString);
        preparedStatement.setString(1, tableSchemaTableName[1]);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            listKeys.add(rs.getString(2));
        }
        return listKeys;
    }

    public List<Map<String, String>> getTableChange(String tableName, List<String> listPrimaryKeys) throws SQLException {
        List<Map<String, String>> listTableChanges = new ArrayList<>();
        String onJoin = "";
        onJoin = "CT." + listPrimaryKeys.get(0) + " = EM." + listPrimaryKeys.get(0);
        if (listPrimaryKeys.size() == 2) {
            onJoin += " AND "+ "CT." + listPrimaryKeys.get(1) + " = EM." + listPrimaryKeys.get(1);
        }

        Map<String, Integer> mapListColumnName = getListColumns(tableName);

        String sql = "SELECT CT.SYS_CHANGE_VERSION, CT.SYS_CHANGE_OPERATION, EM.* " +
                "FROM CHANGETABLE (CHANGES "+ tableName+",0) as CT JOIN "+ tableName +" EM " +
                "ON "+ onJoin +" ORDER BY SYS_CHANGE_VERSION";

        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Map<String, String> mapOneRow = new HashMap<>();
            mapOneRow.put("SYS_CHANGE_OPERATION", rs.getString("SYS_CHANGE_OPERATION"));
            mapOneRow.put("SYS_CHANGE_VERSION", rs.getString("SYS_CHANGE_VERSION"));

            for (Map.Entry<String, Integer> entry : mapListColumnName.entrySet()) {
                mapOneRow.put(entry.getKey(), rs.getString(entry.getKey()));
            }

            listTableChanges.add(mapOneRow);
        }

        return listTableChanges;
    }
}
