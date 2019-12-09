package com.services;

import com.model.Column;

import java.util.List;

public class ListHelper {
    public static Column searchListColumn(List<Column> listColumns, String columnName) {
        Column column = null;
        for (int i = 0; i < listColumns.size(); i++) {
            if (listColumns.get(i).getName().equals(columnName)) {
                column = listColumns.get(i);
                break;
            }
        }
        return column;
    }

    public static boolean checkKeyInDestination(List<String> listKeys, List<Column> listInputColumns) {
        for (int i = 0; i < listKeys.size(); i++) {
            String key = listKeys.get(i);
            boolean findOrNot = false;
            for (int j = 0; j < listInputColumns.size(); j ++) {
                if (listInputColumns.get(j).getName().equals(key)) findOrNot = true;
            }
            if (!findOrNot) {
                return false;
            }
        }
        return true;
    }
}
