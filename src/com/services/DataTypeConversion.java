package com.services;

import com.enums.DataType;
import com.model.Column;

public class DataTypeConversion {
    public static DataType database2Java(String type) {
        DataType out;
        switch (type) {
            case "int":
            case "tinyint":
            case "smallint":
                out = DataType.INT;
                break;
            case "varchar":
            case "nvarchar":
            case "char":
            case "nchar":
                out = DataType.STRING;
                break;
            case "real":
            case "float":
                out = DataType.FLOAT;
                break;
            case "bit":
                out = DataType.BOOLEAN;
                break;
            case "date":
                out = DataType.DATE;
                break;
            case "time":
                out = DataType.TIME;
                break;
            case "timestamp":
                out = DataType.TIMESTAMP;
                break;
            default:
                out = DataType.NOTFOUND;
                break;
        }
        return out;
    }

    public static String buildColumnInSqlString(Column column) {
        String columnInString;
        switch (column.getDataType()) {
            case INT:
                columnInString = column.getName() + " int, \n";
                break;
            case FLOAT:
                columnInString = column.getName() + " float, \n";
                break;
            case BOOLEAN:
                columnInString = column.getName() + " bit , \n";
                break;
            default:
                if (column.getLength() < 0) {
                    columnInString = column.getName() + " nvarchar(" + 100 +"), \n";
                } else {
                    columnInString = column.getName() + " nvarchar(" + column.getLength() +"), \n";
                }
                break;
        }
        return columnInString;
    }
}
