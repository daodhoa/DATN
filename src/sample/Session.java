package sample;

import com.dataflow.DataFlow;
import com.enums.DataType;

import java.util.HashMap;
import java.util.Map;

public final class Session {
    private static DataFlow dataFlow;

    private Session() {}

    public static DataFlow getDataFlow() {
        if(dataFlow == null) {
            dataFlow = new DataFlow();
        }
        return dataFlow;
    }

    public static Map<DataType, String> getMapDataType() {
        Map<DataType, String> mapDataType = new HashMap<>();
        mapDataType.put(DataType.INT, "int");
        mapDataType.put(DataType.BOOLEAN, "boolean");
        mapDataType.put(DataType.DATE, "date");
        mapDataType.put(DataType.FLOAT, "float");
        mapDataType.put(DataType.STRING, "String");
        mapDataType.put(DataType.TIME, "time");
        mapDataType.put(DataType.TIMESTAMP, "timestamp");
        return mapDataType;
    }
}
